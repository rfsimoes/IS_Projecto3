package crawler;

import generated.Cnn;
import generated.Date;
import generated.News;
import generated.Region;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import artifact.*;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class WebCrawler {

	// Criar objecto Cnn
	static Cnn cnn = new Cnn();
	// Criar array de noticias auxiliar
	static ArrayList<News> noticias;
	// Criar array de regiões auxiliar
	static ArrayList<Region> regioes = new ArrayList<Region>();

	/*
	 * Função para fazer o parsing da página e preencher os objetos Java.
	 */
	public void collectNews() {
		System.out.println("Parsing CNN web page");

		String[] region = { "US", "AFRICA", "ASIA", "EUROPE", "LATINAMERICA", "MIDDLEEAST" };
		for (String r : region) {
			System.out.println("\nParsing news from " + r);

			Document doc = null;
			try {
				// Criar uma conexão à página
				doc = Jsoup.connect("http://edition.cnn.com/" + r).timeout(0).get();

				// Fazer o parsing através de ficheiros
				/*
				 * File input = new File("backup/" + r + ".htm"); doc =
				 * Jsoup.parse(input, "UTF-8", "http://edition.cnn.com/");
				 */
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Cannot connect to website");
			}

			// Criar objecto Região
			Region regiao = new Region();
			regiao.setName(r);

			// Criar novo array de notícias
			noticias = new ArrayList<News>();

			/* Ir buscar as notícias "THE LATEST" */
			Elements latest = doc.select("#cnn_mtt1rgtarea a");

			for (Element l : latest) {
				/*
				 * Só interessam notícias dentro do site da CNN e com uma
				 * estrutura comum
				 */
				if (l.attr("abs:href").contains("edition.cnn.com") && !l.attr("abs:href").contains("video")
						&& !l.attr("abs:href").contains("gallery") && !l.attr("abs:href").contains("SPECIALS")
						&& !l.attr("abs:href").contains("SPORT") && !l.attr("abs:href").contains("interactive")) {
					trataNoticia(l.attr("abs:href"));
				}
			}

			/* Ir buscar outras notícias */
			Elements outras = doc.select(".cnn_mtt1imghtitle a");

			for (Element o : outras) {
				/*
				 * Só interessam notícias dentro do site da CNN e com uma
				 * estrutura comum
				 */
				if (o.attr("abs:href").contains("edition.cnn.com") && !o.attr("abs:href").contains("video")
						&& !o.attr("abs:href").contains("gallery") && !o.attr("abs:href").contains("SPECIALS")
						&& !o.attr("abs:href").contains("SPORT") && !o.attr("abs:href").contains("interactive")) {
					trataNoticia(o.attr("abs:href"));
				}
			}

			// Associar notícias à região
			regiao.setNews(noticias);

			// Adicionar região ao array de Regiões
			regioes.add(regiao);

			// Limpar array de notícias
			noticias.clear();
		}

		// Associar regiões ao objecto cnn
		cnn.setRegion(regioes);
	}

	/*
	 * Função para tratar de cada notícia, isto é, recolher as informações
	 * necessárias. Parâmetro de entrada: URL da notícia.
	 */
	private void trataNoticia(String url) {

		/* Criar uma conexão à página */
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(0).get();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("Cannot connect to " + url);
			return;
		}

		// Título da notícia
		String title = doc.select("h1").text();
		System.out.println("Title: " + title);

		// Highlights
		ArrayList<String> highlights = new ArrayList<String>();
		Elements hl = doc.select(".cnn_strylctcntr .cnn_bulletbin li");
		for (Element h : hl) {
			highlights.add(h.text());
		}

		// Data
		String date = doc.select(".cnn_strytmstmp").text();
		Calendar cal = trataData(date);

		// Autor
		ArrayList<String> authors = new ArrayList<String>();
		Elements auth = doc.select(".cnnByLine strong a");
		if (auth.isEmpty())
			auth = doc.select(".cnnByLine strong");
		for (Element a : auth) {
			if (a.text().equals(",") || a.text().equals(", ") || a.text().equals(" ") || a.text().equals("")) {

			} else if (a.text().split(", ").length > 1) {
				authors.add(a.text().split(", ")[0]);
				authors.add(a.text().split(", ")[1]);
			} else {
				authors.add(a.text().split(",")[0]);
			}
		}

		// Texto
		String text = doc.select(".cnn_strycntntlft p").text();

		// URL da foto
		String photoURL = null;
		Elements metaOgImage = doc.select("meta[property=og:image]");
		if (metaOgImage != null) {
			photoURL = metaOgImage.attr("content");
		}

		// URL do vídeo
		String videoURL = doc.getElementsByClass("OUTBRAIN").attr("data-src");

		// Criar objecto News
		News n = new News();
		n.setTitle(title);
		n.setUrl(url);
		n.setHighlights(highlights);
		Date d = new Date();
		d.setDay(cal.get(Calendar.DAY_OF_MONTH));
		d.setMonth(cal.get(Calendar.MONTH) + 1);
		d.setYear(cal.get(Calendar.YEAR));
		int hora = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
		d.setHour(hora);
		n.setDate(d);
		n.setAuthor(authors);
		n.setText(text);
		n.setPhotoURL(photoURL);
		n.setVideoURL(videoURL);

		// Adicionar notícia ao array de notícias
		noticias.add(n);
	}

	/*
	 * Função para criar a data no formato dd/MM/aaa HHmm
	 */
	private Calendar trataData(String data) {
		int[] date = breakDate(data);

		Calendar calendar = new GregorianCalendar(date[2], date[0], date[1], date[3], date[4]);

		return calendar;

	}

	/*
	 * Função para extrair dados necessários da data retirada do site
	 */
	private int[] breakDate(String data) {
		int[] date = new int[5];
		int aux = 0;
		String a;
		StringTokenizer st = new StringTokenizer(data);
		while (st.hasMoreTokens()) {
			a = st.nextToken();

			switch (a) {
			case "January":
				date[aux] = 0;
				aux++;
				break;
			case "February":
				date[aux] = 1;
				aux++;
				break;
			case "March":
				date[aux] = 2;
				aux++;
				break;
			case "April":
				date[aux] = 3;
				aux++;
				break;
			case "May":
				date[aux] = 4;
				aux++;
				break;
			case "June":
				date[aux] = 5;
				aux++;
				break;
			case "July":
				date[aux] = 6;
				aux++;
				break;
			case "August":
				date[aux] = 7;
				aux++;
				break;
			case "September":
				date[aux] = 8;
				aux++;
				break;
			case "October":
				date[aux] = 9;
				aux++;
				break;
			case "November":
				date[aux] = 10;
				aux++;
				break;
			case "December":
				date[aux] = 11;
				aux++;
				break;
			case "--":
			case "Updated":
				break;
			case "GMT":
				return date;
			default:
				if (aux < 3) {
					date[aux] = Integer.parseInt(a.split(",")[0]);
					aux++;
					break;
				} else {
					date[aux] = Integer.parseInt(a.substring(0, 2));
					aux++;
					date[aux] = Integer.parseInt(a.substring(2, 4));
					aux++;
					break;
				}

			}
		}

		return date;
	}

	/*
	 * Função para extrair a informação para um documento XML
	 */
	public void javaToXML(int option) throws InterruptedException {

		try {
			// Create JAXB context and initializing Marshaller
			JAXBContext jc = JAXBContext.newInstance(Cnn.class);
			Marshaller m = jc.createMarshaller();

			// For getting nice formatted output
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			if (option == 1) {
				StringWriter strWriter = new StringWriter();
				m.marshal(cnn, strWriter);
				validateXMLSchema("Example.xsd", strWriter.toString());
				// Specify the name of xml file to be created
				String directory = "C:/CNN/GeradoPeloMarshaller.xml";
				File XMLFile = new File(directory);
				// Writing to XML file
				m.marshal(cnn, XMLFile);
				System.out.println("SAVED IN " + directory + " !");
			} else {
				// Writing to StringWriter
				StringWriter strWriter = new StringWriter();
				m.marshal(cnn, strWriter);
				try {
					AddNewsService sus = new AddNewsService();
					AddNews su = sus.getAddNewsPort();
					System.out.println(su.addNews(strWriter.toString()));
				} catch (Exception e) {
					System.out.println("It wasn't possible to send the XML string. Try later.");
				}
			}

			// Writing to console
			// m.marshal(cnn, System.out);

		} catch (JAXBException e) {
			// e.printStackTrace();
			System.out.println("Failed to marshal data");
		}
	}

	public static boolean validateXMLSchema(String xsdPath, String xmlPath) {

		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlPath)));
			System.out.println("Valid XML!");
		} catch (IOException | SAXException e) {
			System.out.println("Invalid XML!");
			return false;
		}
		return true;
	}

}

package ws;

import javax.jws.WebService;

@WebService
public interface AddNews {
	String addNews(String news);
}

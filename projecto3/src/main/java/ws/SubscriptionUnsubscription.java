package ws;
import javax.jws.WebService;

@WebService
public interface SubscriptionUnsubscription {
	   String subscribe(String email, String region, int soap, int digest);
	   String unsubscribe(String email);
}

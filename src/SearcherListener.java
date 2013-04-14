import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class SearcherListener implements StatusListener{

	private String url;
	private String userKey;
	private String messageKey;
	private ArrayList<String> negatives;
	private boolean isDemo;
	
	public SearcherListener(String url,ArrayList<String> negatives, boolean isDemo, String userKey, String messageKey){
		this.url=url;
		this.userKey=userKey;
		this.messageKey=messageKey;
		this.negatives=negatives;		
		this.isDemo=isDemo;
	}
	
	private void sendStatus(Status status){
	
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.url);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(userKey, status.getUser().getScreenName()));
        params.add(new BasicNameValuePair(messageKey, status.getText() ));
        
        try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			//Execute and get the response.
			if(!isDemo){
				HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
			}
            
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void onStatus(Status status) {
		String lowerStatusText = status.getText().toLowerCase();
        boolean hasNegative =false;
        
        for (String temp : negatives) {
			if(lowerStatusText.contains(temp)){
				hasNegative=true;
				break;
			}
		}
        
        if(!hasNegative ){//&& !status.isRetweet()
        	sendStatus(status);
        	System.out.println("Triggered @" + status.getUser().getScreenName() + " - " + status.getText());
        }else{
        	System.out.println("Not triggered @" + status.getUser().getScreenName() + " - " + status.getText());
        }
        
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        System.out.println("Got stall warning:" + warning);
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }

}

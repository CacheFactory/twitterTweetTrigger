
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import twitter4j.*;

public class Searcher {
	public static void main(String[] args) {

		OptionParser parser = new OptionParser("dk:u:r:g:m:p:");
		OptionSet options = parser.parse(args);

		String keyword = (String) options.valueOf("k");
		String url = (String) options.valueOf("u");
		
		String tweetPostVar="tweet";
		String tweetUserVar="user";
		
		if(options.has("m") ){
			tweetPostVar=(String)options.valueOf("m");
		}
		if(options.has("p") ){
			tweetUserVar=(String)options.valueOf("p");
		}

		boolean isDemo = options.has("d");
		String status= "Searcher started for " + keyword + " for url "+ url;

		if (isDemo) {
			System.out.println("Demo mode");
		}

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		ArrayList<String> negatives = fileToArrayList("negative.txt");
		StatusListener listener = new SearcherListener(url, negatives, isDemo,
				tweetUserVar, tweetPostVar);

		FilterQuery filter = new FilterQuery();
		String[] queries = new String[1];
		queries[0] = keyword;
		filter.track(queries);
		
		if(options.has("g")){
			String coordinates= (String) options.valueOf("g");
			status+= " for location "+coordinates;
			String[] split= coordinates.split(",");
			
			double lat=Double.valueOf(split[0]);
			double lon=Double.valueOf(split[1]);
			double[][] locations={{lat-2,lon-2},{lat+2,lon+2}}; 
			filter.locations(locations);
			
		}
		
		System.out.println(status);
		twitterStream.addListener(listener);
		twitterStream.filter(filter);

	}


	private static ArrayList<String> fileToArrayList(String fileName) {
		ArrayList<String> list = new ArrayList<String>();

		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				list.add(strLine);
			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		return list;
	}
}

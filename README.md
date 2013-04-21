twitterTweetTrigger
===================

Scans tweets for keywords and then triggers a HTTP post request with the tweet and username. 

___

I made this little tool to kick off a workflow in Process Smith, but it could be used in a lot of different ways.

### Usage

You need to enter your Twitter account information in the twitter4j.properties file. 

This is a command line program. The parameters are as follows:
* -k  keywords: A comma seperated list of keywords for the program to scan Tweets for. 
* -u  URL: The URL that will recieve a post request.
* -m  Post Tweet Variable: The HTTP post variable that will contain the tweet.
* -p  Person Variable: The HTTP post variable that will contain the Twitter username.
* -d  Demo mode: Runs in demo mode, no HTTP post will be triggered.

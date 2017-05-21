# Hearthpwn_scraping
Hope I don't cause an accidental DDOS attack

The purpose of this project is to get all the deck names from
the past day with their links and just store that in a file...

Then, because I'm lazy, add a crontab job to run every day at 9AM so then I don't
have to check hearthpwn every day - just add a cat <filename> to my .bashrc and it will print

If I'm more interested in that deck, I can hopefully type in a command that gets me the decklist.

How to run:
go into my-app and run mvn clean install
then go into target and run java -jar HearthScraping-1.0-SNAPSHOT.jar
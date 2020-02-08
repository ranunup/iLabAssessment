# iLabAssessment

To execute the project kindly follow below steps:

Download and install <a href="https://www.docker.com/products/docker-desktop">Docker</a>.

Verify your docker version by issuing command : docker --version, mine is : Docker version 19.03.5, build 633a0ea

###Pull the following docker selenium images:
		* selenium/hub
		* selenium/node-chrome-debug (selenium/node-chrome works too, '-debug' version allows for viewing with vnc server)
		* selenium/node-firefox-debug
To achieve this execute : docker pull <image_name> from your command line.

Once these have been successfully pulled navigate to the root of this project and run the docker-compose.yml file
in detached mode using command: docker-compose up -d, this starts up selenium/hub node and attaches both the chrome
and firefox nodes to the hub node.

To execute test scripts, kindly import this project to Netbeans,
I'm using NetBeans IDE 8.2 (Build 201609300101).
              
Add libraries from the jars.zip folder by right clicking on the Project then navigating to > Properties > Libraries
Also note that I'm using JDK 1.8 for this project.
Import the sql scripts containing test data to your mysql data base and navigate to data.reader > DatabaseReader.java to set your mysql database credentials.

Once all these steps have been successfully executed, run the project.

The console window should indicate the current test step, upon completion you can view test evidence by navigating to the reports folder, this should consist of screenshots as an html test report.

To change the browser you're executing against or test data source navigate to : src > resources > config.properties
and change the webDriver variable or testExecMethod respectively, for brower types the only options catered for are chrome and firefox and for data sources only flat_file and database are catered for.

*All the best :-).

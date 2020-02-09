# iLabAssessment

To execute the project kindly follow below steps:

Import this project into Eclipse,
I'm using Version: 2019-12 (4.14.0) - Build id: 20191212-1212
              
Add libraries from the jars.zip folder by right clicking on the Project then navigating to > Properties > Java Build Path > Libraries
Also note that I'm using JDK 1.8 for this project.

For flat file execution an excel file exists under data/iLabTests.
For database execution please import the sql script named sql_data.sql to your mysql data base and navigate to data.reader > DatabaseReader.java to set your mysql database credentials.

To change the browser you're executing against or test data source navigate to : src > resources > config.properties
and change the webDriver variable or testExecMethod respectively, the only browers catered for are chrome and firefox and data sources catered for are flat_file (Microsoft Excel) and database (MySQL).

Once you have successfully executed the above steps, run the project.

The console window should indicate your current test step, upon completion you can view test evidence by navigating to the reports folder, this should consist of screenshots as well as an html test report.

All the best :-).

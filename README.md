# Message-Processing-DB [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=cjk508_Message-Processing-DB&metric=alert_status)](https://sonarcloud.io/dashboard?id=cjk508_Message-Processing-DB)  [![Build Status](https://travis-ci.com/cjk508/Message-Processing-DB.svg?branch=master)](https://travis-ci.com/cjk508/Message-Processing-DB)
I have chosen to implement this as a RESTful web service as this is slightly more in line with the job specification.
 In the past when I have needed to achieve something similar to this, I have ingested a file located on a mounted folder (SFTP).

## Assumptions
Naturally, I have made a few assumptions on top of those set out in the specification. Here's a list of the main assumptions that I have made:
 -  Adjustments can never cause a historic sale to have a negative value.
 -  The value of each adjustment/sale must be >0.
 -  Only available adjustments operations are Add, Subtract and Multiply
 -  Leading/Trailing spaces in a product name do not change the product itself
 -  Only successfully processed messages count towards the 50 message limit
 -  If an adjustment fails while updating historic sales, then the changes should be rolled back
 -  For the sales report, only two decimal places are needed. For the adjustments, a minimum of 2 decimal places are shown.
## How to run the application
This is a standard spring boot application. It defaults to running on port 8080 but can be changed at
runtime if you have something running on that port.
```
java -jar MessageProcessingDB.jar
```
This will then start the application running on port 8080. You can send messages to the service using the RESTful API.
When the jar is running documentation can be found at this [address](http://localhost:8080/swagger-ui.html)

## Testing
I have used the SpringBoot testing dependencies in this service, meaning Mockito and JUnit 4. 
To make testing easier for you I have also included a directory filled with testing scripts from PostMan which you can import

## Logging & Reporting
For this test I have split the spring logs from the custom logs, you'll be able to find the spring logs in the ```logs/spring.log``` file.
A history of sales can be found in the ```logs/sales.log``` file, these logs are also output to the console.

## Security
If this was getting deployed to a production instance then it would have some form of authentication. For ease
of implementation I have chosen not included these.

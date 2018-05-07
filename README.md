# Message-Processing-DB
I have chosen to implement this as a RESTful web service as this is slightly more in line with the job specification. 
Often when we interact with an external company, we don't want to open a port for them to access as this is another chink
in the armour and leaves it open to abuse. However, I think that the problem lends itself to some form of RESTful interface. 
In the past when I have needed to implement something similar to this, it is through a file that is processed daily through
a mounted folder (SFTP). However, there is an inherent lag in this data which for sales and adjustments is often unacceptable.
## Assumptions
 -  Adjustments can never cause a historic sale to have a negative value.
 -  The value of each adjustment/sale must be >0.
 -  Only available adjustments operations are Add, Subtract and Multiply
 -  Leading/Trailing spaces in a product name do not change the product itself
 -  Only successfully processed messages count towards the 50 message limit
 -  If an adjustment fails while updating historic sales then the changes should be rolled back
## How to run the application
This is a standard spring boot application. It defaults to running on port 8080 but can be changed at
runtime if you have something running on that port.
```
java -jar MessageProcessingDB.jar
```
This will then start the application running on port 8080. You can send messages to the service using the RESTful API.
When the jar is running documentation can be found at this [address](http://localhost:8080/swagger-ui.html)

##Testing
I have used the SpringBoot testing dependencies in this service, meaning Mockito and JUnit 4. 
To make testing easier for you I have also included a directory filled with testing scripts from PostMan which you can import

##Security
If this was getting deployed to a production instance then it would have some form of authentication at the endpoints. For ease
of testing I have not included these.

 
 
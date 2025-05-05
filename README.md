# JavaEE_JMS_SampleProject
A sample project created for my study of JavaEE JMS
- put a Hello msg in a queue
- create consumer and listen to the queue
- get Hello msg

# About this project
- For development environment, I am using VS Code and Docker Desktop in Window WSL with extension `Dev Container` `java:1-17-bookworm`
- and inside dev container, I used extension `redhat.vscode-community-server-connector` for connecting web app server
- JDK17
- GlassFish 7.0.24 as JavaEE web app server (Support Jakarta EE 10, Servlet 6.0, and JDK 11 to JDK 23)
- ActiveMQ 6.1.6 as message broker

# Reference
- I am following instruction of below wordpress post for setup
  - https://geertschuring.wordpress.com/2012/04/20/how-to-connect-glassfish-3-to-activemq-5/

- Following below guide for implementation
  - https://docs.oracle.com/javaee/6/tutorial/doc/gipjg.html
  - https://www.youtube.com/watch?v=qZP2sKLlCfk&t=239s

- Environmental Entry
  - https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/env_entry/env_entry.html

- Schedule in ejb-jar.xml
  - https://stackoverflow.com/questions/21859925/how-to-set-correct-scheduler-time/21861027

# Download
- GlassFish 7.0.24
`wget https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.24.zip`

- ActiveMQ 6.1.6
`wget https://downloads.apache.org/activemq/6.1.6/apache-activemq-6.1.6-bin.tar.gz`
  - To unzip: `tar zxvf apache-activemq-6.1.6-bin.tar.gz`

##### Glassfish Console:
- http://localhost:4848

##### Deflaut HTTP Port:
http://localhost:8080
http://localhost:8080/demo

##### ActiveMQ admin console:
- http://localhost:8161/admin
  - admin/admin

# Setup Active MQ resource in Glassfish
1. Download Active MQ resource adaptor
    - `wget https://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/6.1.6/activemq-rar-6.1.6.rar`

2. Deploy in Glassfish 

3. Setup Resource Adapter Config
  - Everything is default, you don't need change anything e.g. url, user, password don't change

4. Setup Connector Connection Pool
    - follow the Reference
5. Setup Connector Resources
    - follow the Reference

6. you don't need too follow the follow the reference to include these files 
/workspaces/activeMQ/apache-activemq-6.1.6/lib/slf4j-api-2.0.16.jar
/workspaces/activeMQ/apache-activemq-6.1.6/lib/optional/log4j-slf4j2-impl-2.24.3.jar
/workspaces/activeMQ/apache-activemq-6.1.6/lib/optional/log4j-core-2.24.3.jar
/workspaces/activeMQ/apache-activemq-6.1.6/lib/optional/log4j-api-2.24.3.jar

7. Admin Object Resource
    - follow the Reference
    - PhysicalName is the Queue Name in ActiveMQ, it can be anything, it will create for you if not exist in ActiveMQ

# Issue I encountered during development
- Issue 1:
  - `Caused by: java.lang.NullPointerException: Cannot invoke "jakarta.jms.ConnectionFactory.createConnection()" because "this.connectionFactory" is null`

  - Details:
    - When trying to use @Resource to inject ConnectionFactory, it failed to deploy.
    - @Resource need to under EJB or Servlet annotation e.g. @Stateless

- Issue 2:
  - `Caused by: java.lang.UnsupportedOperationException: createContext() is not supported`

  - Details:
    - Apparently, activemq-rar-6.1.6 not do support all JMS 2.0 method
    - cannot use createContext(), you need to use createConnection()
    - cannot use createSession(), you need to use createSession(false, Session.AUTO_ACKNOWLEDGE)
    - Similar case in stackoverflow: https://stackoverflow.com/questions/79039501/activemq-classic-jms-2-0-no-support-for-pooledconnectionfactory
    - for more, please refer to github source code https://github.com/apache/activemq

- Issue 3:
  - When edit setting in Glassfish better restart it to take effective

  - Details:
    - Sometime Glassfish will require you to restart once setting changes
    - but sometime it will not
    - better restart each time to make sure every changes are effective

- Issue 4:
  - Annotation Message Driven Bean not working for remote JMS broker ActiveMQ

  - Details:
    - Trying to implement Message Driven Bean by using annotation @MessageDriven
    - no error encounter in log, but messages were not consumed.

  - Solution:
    - I created a singlaton session bean for managing message consumers
    - use @WebListener and implement ServletContextListener
    - So that we can initialize the message consumer when application startup
    - use @Schedule to check if the connection was started periodically for robustness
    - even the remote ActiveMQ server down it will try to recreate the consumers and reconnect them
    - we need to make sure the connection is start (e.g. connection.start())
    - otherwise, it won't consume message
    - when destory, call connection.close() to release resources
      
- Issue 5:
  - I want to control how many consumers the app will create and how frequence to check the connection status

  - Details:
    - I added ejb-jar.xml in WEB-INF and put the @Schedule setting there
    - Instead using annotation, I used xml, so that the config is not hard coded.
    - I also modify web.xml to have `<env-entry>` and put number of consumers setting there.
    - you can refer to Reference

# Final
- review the wordpress post
  - https://geertschuring.wordpress.com/2012/04/20/how-to-connect-glassfish-3-to-activemq-5/
  - I didn't copy any libraries
  - Message Driven Bean is not working, I create my own consumer
  - I didn't create glassfish-ejb-jar.xml
 
- Next?
  - It is a simple send queue and consum queue JavaEE JMS project
  - I cannot use Message Driven Bean for ActiveMQ, I think I will try different message broker to see it will work
  - I think I can try topic next time
  - and also transaction to build a more robust messaging application

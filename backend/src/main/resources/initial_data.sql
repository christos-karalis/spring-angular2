insert into DIRECTORY (title, description) values ( 'Spring Framework', 'Core support for dependency injection, transaction management, web applications, data access, messaging, testing and more.');
insert into DIRECTORY (title, description) values ( 'Spring Data', 'Spring Data''s mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store.');
insert into DIRECTORY (title, description) values ( 'Spring Integration', 'Spring Integration enables lightweight messaging within Spring-based applications and supports integration with external systems via declarative adapters.');
insert into DIRECTORY (title, description) values ( 'Spring Batch', 'A lightweight, comprehensive batch framework designed to enable the development of robust batch applications vital for the daily operations of enterprise systems.');

insert into THREAD (title, directory_id) values ( 'Spring MVC form Validation: How to remove these lines from getting displayed', 1);
insert into THREAD (title, directory_id) values ( 'Spring Data JPA aggregation group by period of time', 2);
insert into THREAD (title, directory_id) values ( 'How to find all revisions for an entity using spring data envers?', 2);
insert into THREAD (title, directory_id) values ( 'No method readResolve when enabling spring-data-jpa repositories', 2);
insert into THREAD (title, directory_id) values ( 'HTTP 404 error code in spring MVC web app for hello world app', 1);

insert into APP_USER (username, password, firstName, lastName) values ( 'anonymous', '', 'Anonymous', '');
insert into APP_USER (username, password, firstName, lastName) values ( 'user', 'password', 'Averell', 'Dalton');
insert into APP_USER (username, password, firstName, lastName) values ( 'joe', 'password', 'Joe', 'Dalton');
insert into APP_USER (username, password, firstName, lastName) values ( 'luke', 'password', 'Lucky', 'Luke');

insert into POST (body, thread_id, user_id, since) values ( 'I have a simple form and I am performing validations using Spring MVC annotations. I used @size annotation. And when I try to check the error message it gives me this in the webpage Failed to conver', 1, 1, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'at the moment I develop a small eCommerce App with Spring Boot and AngularJS. For my data-access layer I use Spring Data JPA with a MySQL-DB. My next step is, that I want to plot some statistics for specific products. For example: how did the review-ratings from a specific product develop over time. So I would like to specify a period of time (e.g. 01.2016 - 03.2016) and than formulate one query that returns 10 or 15 Points in time (within the range) with the average rating of the reviews in this period. So that i can plot a Chart from that. I found ways with Spring Data to get the average for one period (Between x and y), but then I would have to make 10 queries to the database (for each dot). So I want to know, if it is possible to formulate a query with spring data that splits a time-period in a fixed number of sub-periods and gets the average of customer-ratings within each sub-period? If yes, how can I achieve that?', 2, 2, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'I am using spring-data-envers in my spring boot application. I can successfully log the audits on my entities. Now, I need to show audited data to the user in UI. Like there will be search form where user can select duration and entity for which he wants to see audit logs.', 3, 1, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'I am trying to enable spring data JPA in my webapp, but I am having an issue upon enabling the spring jpa repositories. I am using: Spring 4.2.5 spring-data-jpa 1.9.4 Hibernate 5 JPA 2 (have also tried 2.1) Tomcat 7.0.67 My applicationContext.xml contains: ...', 4, 2, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'I am trying to implement a simple spring mvc web app.I build a project in eclipse And during deployment over tomcat i am getting this error.', 5, 3, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'When you deploy by default, if you have only one application deployed to tomcat, the context root will be ''/''. Unless you have specifically said that it is /desi then you should be using http://localhost:8080/', 5, 4, CURRENT_TIMESTAMP);
insert into POST (body, thread_id, user_id, since) values ( 'In Spring configuration file you have used <context:component-scan base-package="com.theka.desi.Controllers" /> is it the package name where the controllers reside? If no, change it to the actual package. Next, the controller class should have @Controller annotation, without it Spring can''t use other annotations needed to map the requested URL http://localhost:8080/desi/.', 5, 1, CURRENT_TIMESTAMP);






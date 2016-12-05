##AWS-Support-Spring-Boot-Starter

This minimal Spring-Boot-Starter is an very simple EnvironmentPostProcessor
that detects the application is running on AWS or not. It utilizes the
EC2MetadataUtils to fetch some details about the AWS environment it's running on.

To use it, just add the following section to your pom-file:

```
<dependency>
    <groupId>org.zalando.automata</groupId>
    <artifactId>aws-support-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

or this to you Gradle:

```

#TODO

```

###Properties

If the application is starting on AWS it will provide/set the following properties.

| property      | Are           
| ------------- |-------------:|
| aws.az      | Availability-Zone
| aws.region      | Region      
| aws.localhostname | Hostname  
| aws.localipv4 | IP ( v. 4)
| aws.instanceid | Instance-Id 

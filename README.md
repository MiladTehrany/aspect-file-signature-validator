Aspect File Signature Validator
=================================
This example demonstrates usage of TomcatEE for validating file signatures through magic
numbers and other methods ensuring that files meet security and format expectations.

It also showcase integration of TomcatEE and CDI container for injecting CDI beans inside Jax-rs resources.

Contents
--------
The mapping of the URI path for storage API presented in the following table:

 URI path              | Resource class  | HTTP methods | Notes         
-----------------------|-----------------|--------------|---------------
 **_/storage/upload_** | StorageResource | POST         | Uploads file  
 **_/storage/search_** | StorageResource | GET          | Only for test 

Running the Example
-------------------

Run the example as follows:

>     mvn clean compile exec:java

- <http://localhost:8080/api>
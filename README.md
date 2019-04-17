# Search service
Search with elasticsearch on spring boot framework.

Swagger is also enabled on this service.

Service Objectives:
- [x] Search dummy users
- [x] Search dummy challenges

## To Run
1. Download a elasticsearch docker image (i have used official elasticsearch image v6.5.0)
2. Run elasticsearch docker container.
3. Run service.

## Results
The **search** controller will not provide any results because is not pointing to the elasticsearch service.
If you want to search by dummy challenges use **challenge** controller, if you want to search by dummy users use **user** controller.

The main purpose of this rep is to show and improve how to build a elasticsearch SearchRequestBuilder (check builders __package__)

### Swagger
http://localhost:8080/swagger-ui.html




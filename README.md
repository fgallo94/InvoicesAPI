# InvoicesAPI
REST Api for any shop model.
Made with Spring Boot, H2 Database, Project Lombok and JPA.
Can be used also with ShopWallet repository.

## End-Points

*   ### Invoices Controller

End-point       | Http Method | Description
----------------|-------------|-------------
/invoices/      | POST        | Save an invoice
/invoices/{id}  | GET         | Get an Invoice by Id
/invoices/      | GET         | Get all invoices
/invoices/{id}  | POST        | Update a specific invoice


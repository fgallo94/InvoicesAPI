# InvoicesAPI
REST Api for any shop model.
Made with Spring Boot, H2 Database, Project Lombok and JPA.
Can be used also with ShopWallet repository.

## Swagger

[http://localhost:8080/swagger-ui.html](swagger)

### End-Points

*   #### Invoices Controller

End-point                          | Http Method | Description
-----------------------------------|-------------|-------------
/invoices/                         | POST        | Save an invoice
/invoices/                         | GET         | Get all invoices
/invoices/{id}                     | GET         | Get an Invoice by Id
/invoices/{id}                     | POST        | Update a specific invoice
/invoices/{id}                     | DELETE      | Delete a specific invoice
/invoices/{id}/finalize            | POST        | Finalize a specific invoice
/invoices/{id}/pay                 | POST        | Pay a finalized Invoice
/invoices/{id}/line                | POST        | Add a line to invoice
/invoices/{idInvoice}/line/        | DELETE      | Delete all lines
/invoices/{idInvoice}/line/{idLine}| DELETE      | Delete specific line
/invoices/{idInvoice}/line/{idLine}| POST        | Update a specific line
Pet-project based on Spring Cloud technologies (<b>WIP</b>)

---

<center><b>Architecture description</b></center>

There are two profiles for start: `local`; `docker`.

`API-GATEWAY` is up on port 8082

---

<center><b>Query examples</b></center>

<b>User service:</b>

1) POST - New user creation  
   ```bash
   curl --location 'http://localhost:8082/user-service/add' \
   --header 'Content-Type: application/json' \
   --data '{"firstName" : "Sun", "lastName" : "Moon", "phoneNumber" : "89171234567", "companyId" : 1}
   ```
   
2) GET - get user by id
   ```bash
   curl --location 'http://localhost:8082/user-service/get?userId=8'
   ```
   
3) PUT - update user
   ```bash
   curl --location --request PUT 'http://localhost:8082/user-service/update' \
   --header 'Content-Type: application/json' \
   --data '{"id" : 1, "firstName" : "Sun", "lastName" : "Moon", "phoneNumber" : "89171234510", "companyId" : 5}'
   ```
   
4) DELETE - delete user
   ```bash
   curl --location --request DELETE 'http://localhost:8082/user-service/delete?userId=2'
   ```
   
5) GET - get all users
   ```bash
   curl --location 'http://localhost:8082/user-service/all'
   ```

<b>Company service:</b>

1) POST - New company creation
   ```bash
   curl --location 'http://localhost:8082/company-service/add' \
   --header 'Content-Type: application/json' \
   --data '{"companyName" : "Sun Microsystems", "budget" : 12000000.00, "employeeId" : [1, 2, 3]}'
   ```

2) GET - get company by id
   ```bash
   curl --location 'http://localhost:8082/company-service/get?companyId=3'
   ```

3) PUT - update company
   ```bash
   curl --location --request GET 'http://localhost:8082/company-service/get?companyId=3' \
   --header 'Content-Type: application/json' \
   --data '{"id" : 1, "companyName" : "Sun Microsystems", "budget" : 12000000.00, "phoneNumber" : "89171234567", "employeeId" : [1, 2, 3]}'
   ```

4) DELETE - delete company
   ```bash
   curl --location --request DELETE 'http://localhost:8082/company-service/delete?companyId=3'
   ```

5) GET - get all companies
   ```bash
   curl --location 'http://localhost:8082/company-service/all'
   ```
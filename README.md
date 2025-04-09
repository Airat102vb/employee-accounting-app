### Pet-project based on Spring Cloud technologies

---
<center><b>Description</b></center>

Project consists from gateway, eureka server (services registry), config cloud, two eureka client services 
(user-service, company-service).

`user-service` - stores and manipulates with users  
`company-service` - stores and manipulates with companies  
Cross-service requests implemented.

There are two profiles for start: `local`; `docker`.  
`API-GATEWAY` is up on port 8082

---
<center><b>How to launch project</b></center>

1. Clone the project
2. Execute `mvn clean package -DskipTests=true` from root directory
3. Execute `sudo docker compose up -d` from root directory
4. Wait for a while (it takes some time for all services to start)
5. You can test it:
      ```bash
      # Add user
      curl --location 'http://localhost:8082/user-service/add' \
      --header 'Content-Type: application/json' \
      --data '{"firstName" : "Marsel", "lastName" : "Unknown", "phoneNumber" : "89171111112", "companyId" : 1}'
   
      # Create company and add user to the company
      curl --location 'http://localhost:8082/company-service/add' \
      --header 'Content-Type: application/json' \
      --data '{"companyName" : "Sun Microsystems", "budget" : 12000000.00, "employeeId" : [1, 2]}'
   
      # Get all companies with users
      curl --location 'http://localhost:8082/company-service/all'
      ```
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
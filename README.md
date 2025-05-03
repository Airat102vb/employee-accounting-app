### Pet-project based on Spring Cloud technologies

---
<p style="text-align: center;"><b>Description</b></p>  

<b>Project consists from:</b>  
- <b>Spring tech:</b> gateway, eureka server, config cloud, two eureka clients (user-service, company-service);  
- <b>Other tech:</b> PostgreSQL, Hibernate, Docker compose.

`user-service` - stores and manipulates with users  
`company-service` - stores and manipulates with companies  
Cross-service requests implemented.

There are two profiles for start: `local`; `docker`.  
`API-GATEWAY` is up on port 8082

---
<p style="text-align: center;"><b>How to launch project</b></p>

1. Clone the project
2. Execute `mvn clean package -DskipTests=true` from root directory
3. Execute `sudo docker compose up -d` from root directory
4. Wait for a while (it takes some time for all services to start)
5. You can test it:
      ```bash
      # Add user
      curl --location 'http://localhost:8082/user-service/user' \
      --header 'Content-Type: application/json' \
      --data '{"firstName" : "Fedor", "lastName" : "Dostoevsky", "phoneNumber" : "89171111117", "companyId" : 1}'
   
      # Create company and add user to the company
      curl --location 'http://localhost:8082/company-service/company' \
      --header 'Content-Type: application/json' \
      --data '{"companyName" : "Cisco", "budget" : 17000000.50, "employeeId" : [1]}'
   
      # Get all companies with users
      curl --location 'http://localhost:8082/company-service/company'
   
      # Get users by list of ids
      curl --location 'http://localhost:8082/user-service/user' \
      --header 'X-Employee-IDs: 1,2'
      ```
   **OR** by launch `generate_test_data.py` and test by

   ```bash
   # Get users with pagination
   curl --location 'http://localhost:8082/user-service/user/page?pageNumber=0&pageSize=2'
   
   # Get companies with pagination
   curl --location 'http://localhost:8082/company-service/company/page?pageNumber=0&pageSize=2'
   ```
---
<b>Query examples: user-service</b>  

1) POST - New user creation  
   ```bash
   curl --location 'http://localhost:8082/user-service/user' \
   --header 'Content-Type: application/json' \
   --data '{"firstName" : "Fedor", "lastName" : "Dostoevsky", "phoneNumber" : "89171111117", "companyId" : 1}'
   ```
   
2) GET - get user by id
   ```bash
   curl --location 'http://localhost:8082/user-service/user/1?withCompanyInfo=true'
   ```
   
3) PUT - update user
   ```bash
   curl --location --request PUT 'http://localhost:8082/user-service/user/3' \
   --header 'Content-Type: application/json' \
   --data '{"firstName" : "Fedor", "lastName" : "Dostoevsky", "phoneNumber" : "89171111121", "companyId" : 1}'
   ```
   
4) DELETE - delete user
   ```bash
   curl --location --request DELETE 'http://localhost:8082/user-service/user/1'
   ```
   
5) GET - get all users
   ```bash
   curl --location 'http://localhost:8082/user-service/user'
   ```

<b>Query examples: company-service</b>  

1) POST - New company creation
   ```bash
   curl --location 'http://localhost:8082/company-service/company' \
   --header 'Content-Type: application/json' \
   --data '{"companyName" : "Cisco", "budget" : 17000000.50, "employeeId" : [1]}'
   ```

2) GET - get company by id
   ```bash
   curl --location 'http://localhost:8082/company-service/company/1?withUserInfo=true'
   ```

3) PUT - update company
   ```bash
   curl --location --request PUT 'http://localhost:8082/company-service/company/1' \
   --header 'Content-Type: application/json' \
   --data '{"companyName" : "Cisco", "budget" : 17000000.50, "employeeId" : [1, 2]}'
   ```

4) DELETE - delete company
   ```bash
   curl --location --request DELETE 'http://localhost:8082/company-service/company/2'
   ```

5) GET - get all companies
   ```bash
   curl --location 'http://localhost:8082/company-service/company'
   ```
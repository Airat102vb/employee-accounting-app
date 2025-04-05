Pet-project based on Spring Cloud technologies (WIP)

---

<b>Architecture description</b> 

API-GATEWAY is up on port 8082

---

<b>Query examples:</b>

1) POST - New user creation  
   ```bash
   curl --location 'http://localhost:8082/user-service/add' \
   --header 'Content-Type: application/json' \
   --data '{"firstName" : "Sun", "lastName" : "Moon", "phoneNumber" : "89171234567", "companyId" : 1}
   ```
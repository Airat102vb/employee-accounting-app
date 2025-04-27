import requests;

test_user_data = [["Федор", "Достоевский", "79171111111", 1],
                  ["Лев", "Толстой", "79171111112", 2],
                  ["Сергей", "Есенин", "79171111113", 3],
                  ["Николай", "Карамзин", "79171111114", 1],
                  ["Марк", "Аврелий", "79171111115", 2]]

test_company_data = [["Cisco", 15000000.50, [1, 4]],
                     ["JetBrains", 30000000.75, [2, 5]],
                     ["Google", 50000000.99, [3]]]

for user_data in test_user_data:
    first_name, last_name, phone_number, company_id = user_data
    json_data = {
        "firstName": first_name,
        "lastName": last_name,
        "phoneNumber": phone_number,
        "companyId": company_id
    }
    r = requests.post(
        "http://localhost:8082/user-service/user",
        json=json_data
    )
    print(json_data)
    print(r.status_code, r.text, '\n')

for company_data in test_company_data:
    company_name, budget, employee_id = company_data
    json_data = {
        "companyName": company_name,
        "budget": budget,
        "employeeId": employee_id
    }
    r = requests.post(
        "http://localhost:8082/company-service/company",
        json=json_data
    )
    print(json_data)
    print(r.status_code, r.text, '\n')
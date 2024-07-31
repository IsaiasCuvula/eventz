# Events API

# Base Url: http://localhost:8080/v1

## Endpoints 

HTTP Method − GET
URL − `http://localhost:8080/v1/eventz?page=1&size=10`

HTTP Method − POST: 
URL − `http://localhost:8080/v1/eventz` //include the body

HTTP Method − UPDATE:
URL − `http://localhost:8080/v1/eventz/{id}`

HTTP Method − DELETE: 
URL − `http://localhost:8080/v1/eventz/{id}`


### Search Events

- **Search by Title:**
`http://localhost:8080/v1/eventz?title=concert&page=0&size=10`

- **Search by Date:**
`http://localhost:8080/v1/eventz?date=2024-08-01&page=0&size=10`

- **Search by Location:**
Example: `http://localhost:8080/v1/eventz?location=New%20York&page=0&size=10`

- **Combined Search (Title, Date, and Location):**
Example: `http://localhost:8080/v1/eventz?title=concert&date=2024-08-01&location=New%20York&page=0&size=10`

### Filter Events by Category

- **Filter by Category:**
Example: `http://localhost:8080/v1/eventz?category=music&page=0&size=10`

### Upcoming Events

- **Upcoming Events:**
Example: `http://localhost:8080/v1/eventz/upcoming?page=0&size=10`

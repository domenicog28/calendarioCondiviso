# Calendario Condiviso

## Descrizione

Backend di una web-app per un Calendario condiviso. Offre la possibilità di registrare Organizzazioni e tramite invito di creare nuovi Utenti. Organizzazioni e Utenti possono gestire eventi con creazione, modifica ed eliminazione di un nuovo evento. Gli eventi possono essere personali (creati dall'utente) e di team (creati solo dall'organizzazione per tutti gli utenti). Il progetto è sviluppato seguendo il pattern **TDD (Test-Driven Development)** con test di integrazione sui Repository tramite **Testcontainers** e unit test sui Service tramite **Mockito**.

## Tech e Stack

- Spring Boot
- PostgreSQL
- Docker
- Maven
- Flyway
- MapStruct
- Lombok
- Junit5
- Mockito
- Testcontainers
- JWT

## Prerequisiti

- Docker Desktop
- Postman

## Come avviare il Server
Da console

```bash
# 1. Clona la repository
git clone https://github.com/tuouser/calendarioCondiviso.git

# 2. Copia il file delle variabili d'ambiente e modifica il .env con i tuoi valori
cp .env.example .env

# 3. Avvia i container Docker
docker compose up --build -d

```
&nbsp;
## Variabili d'ambiente

Copia il file `.env.example` in `.env` e compila con i tuoi valori:

| Variabile | Descrizione |
| --- | --- |
| DB_NAME | Nome del database PostgreSQL |
| DB_USERNAME | Username del database |
| DB_PASSWORD | Password del database |
| DB_PORT | Porta del database (default: 5432) |
| DB_HOST | postgres se avviata con docker o localhost se avviata da IDE |
| PGADMIN_EMAIL | Email per accedere a pgAdmin |
| PGADMIN_PASSWORD | Password per pgAdmin |
| JWT_SECRET | Chiave segreta JWT (min. 32 caratteri) |
| JWT_ACCESS_TOKEN_EXP | Durata access token in minuti |
| JWT_REFRESH_TOKEN_EXP | Durata refresh token in minuti |

## Endpoints API

Utilizzare PostMan

### Autenticazione
| Metodo | URL | Descrizione |
| --- | --- | --- |
| POST | /auth/login | Login organizzazione o utente |
| POST | /auth/refresh | Rinnova access token |

### Organizzazioni
| Metodo | URL | Descrizione |
| --- | --- | --- |
| POST | /organizzazioni/registrazione | Registra nuova organizzazione |
| GET | /organizzazioni/{uuid} | Recupera dati organizzazione |
| PUT | /organizzazioni/modifica/{uuid} | Modifica dati organizzazione |
| DELETE | /organizzazioni/elimina/{uuid} | Elimina organizzazione |
| GET | /organizzazioni/{uuid}/utenti | Recupera utenti organizzazione |
| GET | /organizzazioni/{uuid}/eventi | Recupera eventi organizzazione |

### Utenti
| Metodo | URL | Descrizione |
| --- | --- | --- |
| POST | /utenti/registrazione/{uuidOrganizzazione} | Registra nuovo utente |
| GET | /utenti/{uuid} | Recupera dati utente |
| PUT | /utenti/modifica/{uuid} | Modifica dati utente |
| DELETE | /utenti/elimina/{uuid} | Elimina utente |
| GET | /utenti/{uuid}/eventi | Recupera eventi utente |

### Eventi
| Metodo | URL | Descrizione |
| --- | --- | --- |
| POST | /eventi/registrazione | Registra nuovo evento |
| GET | /eventi/{uuid} | Recupera dati evento |
| PUT | /eventi/modifica/{uuid} | Modifica dati evento |
| DELETE | /eventi/elimina/{uuid} | Elimina evento |

### Inviti
| Metodo | URL | Descrizione |
| --- | --- | --- |
| POST | /inviti/registrazione | Registra nuovo invito |


## Autenticazione
Per gli endpoints diversi da:

| Metodo | URL |
| --- | --- |
| POST | /auth/login |
| POST | /auth/refresh |
| POST | /organizzazioni/registrazione |
| POST | /utenti/registrazione/{uuidOrganizzazione} |

è necessario effettuare il login, recuperare dal Header della risposta di login l'access Token e impostarlo nel Header della nuova richiesta (Key: Authorization value: Bearer + accessToken)

### Test API
Gli endpoints possono essere testati tramite Postman

### Request Body
`/auth/login`

```json
{
    "email": "esempio@gmail.com",
    "password": "password123"
}
```

`/organizzazioni/registrazione`

```json
{
    "email": "esempio@gmail.com",
    "password": "password123",
    "descrizione": "Nome Azienda SRL"
}
```


`/organizzazioni/modifica/{uuid}`

```json
{
    "password": "nuovaPassword123",
    "descrizione": "Nuova descrizione"
}
```


`/utenti/registrazione/{uuidOrganizzazione}`

```json
{
    "email": "utente@gmail.com",
    "password": "password123",
    "nome": "Mario",
    "cognome": "Rossi"
}
```

`/utenti/modifica/{uuid}`

```json
{
    "password": "nuovaPassword123",
    "nome": "Luigi",
    "cognome": "Bianchi"
}
```

`/eventi/registrazione`

```json
{
    "idOrganizzazione": "uuid-organizzazione",
    "tipo": "TEAM",
    "titolo": "Riunione settimanale",
    "descrizione": "Riunione del team",
    "timeInizio": "2026-06-10T10:00:00+02:00",
    "timeFine": "2026-06-10T11:00:00+02:00",
    "timeNotifica": "2026-06-10T09:00:00+02:00"
}
```

`/eventi/modifica/{uuid}`

```json
{
    "titolo": "Nuovo titolo",
    "descrizione": "Nuova descrizione",
    "timeInizio": "2026-06-10T10:00:00+02:00",
    "timeFine": "2026-06-10T11:00:00+02:00",
    "timeNotifica": "2026-06-10T09:00:00+02:00"
}
```

`/inviti/registrazione`

```json
{
    "idOrganizzazione": "uuid-organizzazione"
}
```


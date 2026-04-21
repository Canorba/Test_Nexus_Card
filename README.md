Bank Inc - Card & Transaction API
Sistema backend desarrollado en Spring Boot para la gestión de tarjetas (débito/crédito) y transacciones bancarias.  
Permite la emisión de tarjetas, gestión de saldo, compras y reverso de transacciones.
---
Tecnologías utilizadas
Java 25+
Spring Boot
Spring Data JPA
Hibernate
PostgreSQL (configurable)
Lombok
Maven
---
Arquitectura del proyecto
El proyecto sigue una arquitectura en capas:
controller → Exposición de endpoints REST  
service → Lógica de negocio  
repository → Acceso a base de datos  
entity → Modelo de datos  
dto → Objetos de transferencia  
exception → Manejo de errores (opcional)  
config → Configuración del sistema
---
Modelo de datos
Card
cardId (PK) → 16 dígitos (6 productId + 10 aleatorios)
productId → identificador del producto
holderName → nombre del titular
expiryDate → fecha de vencimiento (+3 años)
balance → saldo disponible
active → estado de activación
blocked → estado de bloqueo
Transaction
transactionId (PK)
cardId (FK lógico)
price → valor de la compra
date → fecha de transacción
status → APPROVED / REVERSED
---
Cómo ejecutar el proyecto
1. Clonar repositorio
```bash
git clone https://github.com/Canorba/bankinc-card-system.git
```
2. Ir al directorio
```bash
cd bankinc-card-system
```
3. Ejecutar aplicación
```bash
mvn spring-boot:run
```
4. Acceder a la aplicación
```
http://localhost:8080
```
---
Endpoints del sistema
Card API
1. Generar tarjeta
GET /card/{productId}/number
2. Activar tarjeta
POST /card/enroll
3. Bloquear tarjeta
DELETE /card/{cardId}
4. Recargar saldo
POST /card/balance
5. Consultar saldo
GET /card/balance/{cardId}
---
🛒 Transaction API
6. Realizar compra
POST /transaction/purchase
7. Consultar transacción
GET /transaction/{transactionId}
8. Anular transacción (máx. 24 horas)
POST /transaction/anulation
---
Reglas de negocio
Tarjetas
16 dígitos obligatorios
6 primeros = productId
Expiran en 3 años
Inician inactivas
No operan si están bloqueadas
Transacciones
Requiere tarjeta activa
Debe haber saldo suficiente
No permite compra con tarjeta vencida
Anulación solo en 24 horas
Reintegro automático del saldo
---
Pruebas
```bash
mvn test
```
Cobertura mínima: 80%
---

Postman
Importar colección incluida en:
```
/postman/BankInc-Card-System.postman\_collection.json
```
---

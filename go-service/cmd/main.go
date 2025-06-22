package main

import (
    "github.com/gofiber/fiber/v2"
    "github.com/your-org/go-transaction-service/db"
    "github.com/your-org/go-transaction-service/routes"
)

func main() {
    db.Init()
    app := fiber.New()
    routes.Setup(app)
    app.Listen(":8081")
}

package db

import (
    "log"
    "gorm.io/driver/sqlite"
    "gorm.io/gorm"
    "github.com/your-org/go-transaction-service/internal/model"
)

var DB *gorm.DB

func Init() {
    var err error
    DB, err = gorm.Open(sqlite.Open("transactions.db"), &gorm.Config{})
    if err != nil {
        log.Fatalf("Failed to connect to SQLite: %v", err)
    }

    if err := DB.AutoMigrate(&model.Transaction{}); err != nil {
        log.Fatalf("Failed to auto-migrate schema: %v", err)
    }
}

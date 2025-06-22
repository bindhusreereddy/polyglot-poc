package service

import (
    "github.com/google/uuid"
    "github.com/your-org/go-transaction-service/internal/model"
    "github.com/your-org/go-transaction-service/db"
)

func Create(tx model.Transaction) (model.Transaction, error) {
    tx.ID = uuid.New().String()
    tx.Status = "PENDING"
    if err := db.DB.Create(&tx).Error; err != nil {
        return tx, err
    }
    return tx, nil
}

func GetByID(id string) (model.Transaction, error) {
    var tx model.Transaction
    if err := db.DB.First(&tx, "id = ?", id).Error; err != nil {
        return tx, err
    }
    return tx, nil
}

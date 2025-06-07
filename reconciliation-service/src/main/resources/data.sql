INSERT INTO transaction_log (transaction_id, amount, status, merchant_id, transaction_date)
VALUES
    ('TXN001', 100.00, 'SUCCESS', 'MERCH001', '2025-06-06T10:00:00'),
    ('TXN002', 200.00, 'SUCCESS', 'MERCH001', '2025-06-06T12:00:00'),
    ('TXN003', 150.00, 'FAILED', 'MERCH001', '2025-06-06T14:00:00'),
    ('TXN004', 300.00, 'SUCCESS', 'MERCH002', '2025-06-06T16:00:00');
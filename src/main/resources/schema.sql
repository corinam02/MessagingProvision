-- 1. Messaging Service
CREATE TABLE messages (
                          id VARCHAR(36) PRIMARY KEY,
                          tenant_id VARCHAR(100) NOT NULL,
                          from_number VARCHAR(20) NOT NULL,
                          to_number VARCHAR(20) NOT NULL,
                          body TEXT,
                          status VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_msg_tenant ON messages(tenant_id);

-- 2. Provisioning Service
CREATE TABLE phone_numbers (
                               id VARCHAR(36) PRIMARY KEY,
                               phone_number VARCHAR(20) UNIQUE NOT NULL,
                               client_id VARCHAR(100),
                               status VARCHAR(20),
                               monthly_price DECIMAL(10, 2)
);

-- 3. Billing Service
CREATE TABLE billing_records (
                                 id VARCHAR(36) PRIMARY KEY,
                                 event_id VARCHAR(100) UNIQUE NOT NULL,
                                 event_type VARCHAR(30) NOT NULL,
                                 tenant_id VARCHAR(100) NOT NULL,
                                 total_amount DECIMAL(10, 6) NOT NULL,
                                 occurred_at TIMESTAMP NOT NULL,
                                 recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bill_tenant ON billing_records(tenant_id);
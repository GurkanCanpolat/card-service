CREATE TABLE customers (
                           id UUID PRIMARY KEY,
                           full_name VARCHAR(255),
                           email VARCHAR(255),
                           phone_number VARCHAR(255),
                           created_at TIMESTAMPTZ,
                           updated_at TIMESTAMPTZ
);

CREATE TABLE cards (
                       id UUID PRIMARY KEY,
                       customer_id UUID,
                       card_number VARCHAR(255),
                       card_type VARCHAR(255),
                       status VARCHAR(255),
                       created_at TIMESTAMPTZ,
                       updated_at TIMESTAMPTZ
);

CREATE TABLE card_limits (
                             id UUID PRIMARY KEY,
                             card_id UUID,
                             total_limit NUMERIC(19, 2),
                             available_limit NUMERIC(19, 2),
                             created_at TIMESTAMPTZ,
                             updated_at TIMESTAMPTZ
);

CREATE TABLE card_transactions (
                                   id UUID PRIMARY KEY,
                                   request_id UUID,
                                   card_id UUID,
                                   original_transaction_id UUID,
                                   amount NUMERIC(19, 2),
                                   currency VARCHAR(10),
                                   type VARCHAR(255),
                                   status VARCHAR(255),
                                   created_at TIMESTAMPTZ,
                                   updated_at TIMESTAMPTZ
);

CREATE TABLE outbox_events (
                               id UUID PRIMARY KEY,
                               aggregate_type VARCHAR(255),
                               aggregate_id UUID,
                               event_type VARCHAR(255),
                               payload TEXT,
                               status VARCHAR(255),
                               error_message VARCHAR(1000),
                               created_at TIMESTAMPTZ,
                               published_at TIMESTAMPTZ
);
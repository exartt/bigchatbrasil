CREATE TABLE IF NOT EXISTS client (
    id_client BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    telephone TEXT NOT NULL,
    cpf TEXT NOT NULL,
    cnpj TEXT NOT NULL,
    company_name TEXT NOT NULL,
    plan_type PlanType NOT NULL,
    whatsapp BOOL NOT NULL DEFAULT false,
    entity_uid UUID NOT NULL DEFAULT uuid_generate_v4(),
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS client_prepaid (
    id_client_pre BIGSERIAL PRIMARY KEY,
    id_client INT8 NOT NULL REFERENCES client(id_client),
    amount_credit FLOAT4 NOT NULL,
    entity_uid UUID NOT NULL DEFAULT uuid_generate_v4(),
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS client_postpaid (
    id_client_pos BIGSERIAL PRIMARY KEY,
    id_client INT8 NOT NULL REFERENCES client(id_client),
    limit_value FLOAT4 NOT NULL,
    spent_value FLOAT4 NOT NULL DEFAULT 0,
    entity_uid UUID NOT NULL DEFAULT uuid_generate_v4(),
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS client_message (
    id_client_message BIGSERIAL PRIMARY KEY,
    id_client INT8 NOT NULL REFERENCES client(id_client),
    telephone TEXT NOT NULL,
    whatsapp BOOL NOT NULL DEFAULT false,
    text TEXT NOT NULL,
    sid TEXT NOT NULL,
    entity_uid UUID NOT NULL DEFAULT uuid_generate_v4(),
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

INSERT INTO client (name, email, telephone, cpf, cnpj, company_name, plan_type)
VALUES
('João Silva', 'joao.silva@example.com', '11987654321', '123.456.789-00', '12.345.678/0001-00', 'Empresa do João', 'PREPAID'),
('Maria Souza', 'maria.souza@example.com', '21987654322', '987.654.321-00', '98.765.432/0001-00', 'Empresa da Maria', 'POSTPAID'),
('Ana Torres', 'ana.torres@example.com', '21987654323', '111.222.333-44', '11.222.333/0001-11', 'Empresa da Ana', 'PREPAID'),
('Carlos Gomes', 'carlos.gomes@example.com', '31987654324', '444.555.666-77', '44.555.666/0001-44', 'Empresa do Carlos', 'POSTPAID');

INSERT INTO client_prepaid (id_client, amount_credit)
VALUES
((SELECT id_client FROM client WHERE email = 'joao.silva@example.com'), 1000.00);

INSERT INTO client_postpaid (id_client, limit_value)
VALUES
((SELECT id_client FROM client WHERE email = 'maria.souza@example.com'), 5000.00);

INSERT INTO client_prepaid (id_client, amount_credit)
VALUES
((SELECT id_client FROM client WHERE email = 'ana.torres@example.com'), 1500.00);

INSERT INTO client_postpaid (id_client, limit_value)
VALUES
((SELECT id_client FROM client WHERE email = 'carlos.gomes@example.com'), 3000.00);
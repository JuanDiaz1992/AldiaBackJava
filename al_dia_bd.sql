-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-05-2025 a las 19:42:52
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `al_dia_bd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `audit_log`
--

CREATE TABLE `audit_log` (
  `id_log` int(11) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `record_id` int(11) NOT NULL,
  `action` varchar(10) NOT NULL,
  `old_values` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`old_values`)),
  `new_values` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`new_values`)),
  `changed_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `changed_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `audit_log`
--

INSERT INTO `audit_log` (`id_log`, `table_name`, `record_id`, `action`, `old_values`, `new_values`, `changed_at`, `changed_by`) VALUES
(1, 'expenses', 7, 'UPDATE', '{\"amount\": 600000, \"date\": \"2025-04-17\", \"category\": 2}', '{\"amount\": 899998, \"date\": \"2025-04-17\", \"category\": 2}', '2025-05-21 02:45:16', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `category_expenses`
--

CREATE TABLE `category_expenses` (
  `id_category` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `category_expenses`
--

INSERT INTO `category_expenses` (`id_category`, `name`) VALUES
(1, 'Alquiler o hipoteca'),
(2, 'Supermercado y alimentos'),
(3, 'Transporte (gasolina, transporte público)'),
(4, 'Servicios públicos (luz, agua, gas)'),
(5, 'Internet y telefonía'),
(6, 'Seguros (salud, auto, hogar)'),
(7, 'Entretenimiento (cine, streaming, salidas)'),
(8, 'Ropa y calzado'),
(9, 'Educación (cursos, colegio, universidad)'),
(10, 'Cuidado personal (peluquería, cosméticos)'),
(11, 'Impuestos'),
(12, 'Salud (medicinas, consultas médicas)'),
(13, 'Mantenimiento del hogar'),
(14, 'Electrodomésticos y tecnología'),
(15, 'Regalos y donaciones'),
(16, 'Vacaciones y viajes'),
(17, 'Mascotas (veterinario, alimento)'),
(18, 'Deportes y gimnasio'),
(19, 'Ahorros e inversiones'),
(20, 'Gastos bancarios (comisiones)');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `category_incomes`
--

CREATE TABLE `category_incomes` (
  `id_category` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `category_incomes`
--

INSERT INTO `category_incomes` (`id_category`, `name`) VALUES
(1, 'Salario (empleo principal)'),
(2, 'Ingresos por freelance o proyectos independientes'),
(3, 'Bonos o incentivos laborales'),
(4, 'Comisiones por ventas'),
(5, 'Ingresos por alquiler de propiedades'),
(6, 'Dividendos de inversiones'),
(7, 'Intereses bancarios (cuentas de ahorro)'),
(8, 'Pensión o jubilación'),
(9, 'Subsidios gubernamentales'),
(10, 'Becas educativas'),
(11, 'Premios o concursos ganados'),
(12, 'Venta de artículos personales'),
(13, 'Ingresos por patrocinios'),
(14, 'Regalías (libros, música, patentes)'),
(15, 'Herencia recibida'),
(16, 'Reembolsos (impuestos, seguros)'),
(17, 'Ingresos por agricultura o cultivos'),
(18, 'Ganancias de apuestas o lotería'),
(19, 'Ingresos por mentoring o consultoría'),
(20, 'Ayudas familiares');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `email_verification`
--

CREATE TABLE `email_verification` (
  `id_email_verification` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `expiration_time` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `expenses`
--

CREATE TABLE `expenses` (
  `id_expense` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_planned` bit(1) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `expenses`
--

INSERT INTO `expenses` (`id_expense`, `amount`, `date`, `description`, `is_planned`, `picture`, `category`, `id_user`) VALUES
(1, 120000, '2025-05-20', 'Pago factura Tigo', b'0', '', 5, 1),
(2, 15000, '2025-05-20', 'Netflix', b'0', '', 7, 1),
(3, 800000, '2025-05-20', 'Mercado', b'0', '', 2, 1),
(4, 10000000, '2025-03-21', 'Compra moto', b'0', '', 3, 1),
(5, 800000, '2025-05-20', 'Pago arriendo', b'0', '', 1, 1),
(6, 500000, '2025-05-20', 'Pago Cuota del banco', b'0', '', 20, 1),
(7, 899998, '2025-04-17', 'Compra mercado', b'0', '', 2, 1);

--
-- Disparadores `expenses`
--
DELIMITER $$
CREATE TRIGGER `after_expense_update` AFTER UPDATE ON `expenses` FOR EACH ROW BEGIN
    INSERT INTO audit_log (table_name, record_id, action, old_values, new_values, changed_by)
    VALUES ('expenses', NEW.id_expense, 'UPDATE',
            JSON_OBJECT('amount', OLD.amount, 'date', OLD.date, 'category', OLD.category),
            JSON_OBJECT('amount', NEW.amount, 'date', NEW.date, 'category', NEW.category),
            NEW.id_user);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `heritages`
--

CREATE TABLE `heritages` (
  `id_heritage` int(11) NOT NULL,
  `acquisition_date` date DEFAULT NULL,
  `acquisition_value` int(11) DEFAULT NULL,
  `curren_value` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type_heritages` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `percentage` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `heritages`
--

INSERT INTO `heritages` (`id_heritage`, `acquisition_date`, `acquisition_value`, `curren_value`, `description`, `type_heritages`, `id_user`, `location`, `percentage`) VALUES
(1, '2025-05-07', NULL, 100000000, 'Casa de dos pisos y parqueadero', 1, 1, 'Calle 15#3-15 Bogotá', 30);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `incomes`
--

CREATE TABLE `incomes` (
  `id_income` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_planned` bit(1) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `incomes`
--

INSERT INTO `incomes` (`id_income`, `amount`, `date`, `description`, `is_planned`, `picture`, `category`, `id_user`) VALUES
(1, 2500000, '2025-05-20', 'Quincena', b'0', '', 1, 1),
(2, 2500000, '2025-04-20', 'Sueldo', b'0', '', 1, 1),
(3, 1500000, '2025-05-20', 'Pago página web', b'0', '', 2, 1),
(4, 300000, '2025-04-23', 'CDT', b'0', '', 7, 1),
(5, 12000000, '2025-03-12', 'Me gane la lotería', b'0', '', 18, 1),
(6, 20000000, '2024-10-22', 'Me gane el chance', b'0', '', 11, 1),
(7, 7999997, '2024-03-19', 'sueldo ', b'0', '', 1, 1),
(8, 60000000, '2025-03-13', 'Comiciones por ventas de vehiculos en el año', b'0', '', 4, 1),
(9, 1200000, '2025-05-25', 'Pago de cliente', b'0', NULL, 2, 1),
(10, 133999, '2025-05-25', 'Pago de cliente por página web', b'0', NULL, 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `passwordrestoretoken`
--

CREATE TABLE `passwordrestoretoken` (
  `id_restorepassword` int(11) NOT NULL,
  `expiration_time` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `id_user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profiles`
--

CREATE TABLE `profiles` (
  `id_profile` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birth_date` varchar(255) DEFAULT NULL,
  `civil_status` tinyint(4) DEFAULT NULL,
  `data_treatment` bit(1) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `document` varchar(255) DEFAULT NULL,
  `exogenous` int(11) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `number_phone` varchar(255) DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `type_document` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `profiles`
--

INSERT INTO `profiles` (`id_profile`, `address`, `birth_date`, `civil_status`, `data_treatment`, `department`, `document`, `exogenous`, `last_name`, `name`, `number_phone`, `occupation`, `profile_picture`, `surname`, `town`, `type_document`) VALUES
(1, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'Díaz', 'Juan Camilo', NULL, NULL, NULL, 'Valencia', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `type_heritages`
--

CREATE TABLE `type_heritages` (
  `id_type_heritage` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `type_heritages`
--

INSERT INTO `type_heritages` (`id_type_heritage`, `name`) VALUES
(1, 'Casa'),
(2, 'Apartamento'),
(3, 'Terreno'),
(4, 'Vehículo automotor'),
(5, 'Embarcación'),
(6, 'Empresa/unidad productiva'),
(7, 'Acciones en bolsa'),
(8, 'Participación en sociedades'),
(9, 'Fondos de inversión'),
(10, 'Depósitos bancarios'),
(11, 'Cuentas de ahorro'),
(12, 'Planes de pensiones'),
(13, 'Seguros de vida con ahorro'),
(14, 'Derechos de autor/patentes'),
(15, 'Franquicias/licencias'),
(16, 'Créditos a terceros'),
(17, 'Metales preciosos (oro, plata)'),
(18, 'Criptomonedas'),
(19, 'Colecciones de arte'),
(20, 'Joyas y objetos de valor');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_from_external_app` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` tinyint(4) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `id_profile` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id_user`, `email`, `is_from_external_app`, `password`, `role`, `username`, `id_profile`) VALUES
(1, 'juannavegante2010@gmail.com', b'0', '$2a$10$1TMBtntnuqNdSgBlepINRe5ngWFmF1DqNNQ9rKXd7lq7GBdOwDS8G', 2, 'juannavegante2010.1', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `audit_log`
--
ALTER TABLE `audit_log`
  ADD PRIMARY KEY (`id_log`);

--
-- Indices de la tabla `category_expenses`
--
ALTER TABLE `category_expenses`
  ADD PRIMARY KEY (`id_category`);

--
-- Indices de la tabla `category_incomes`
--
ALTER TABLE `category_incomes`
  ADD PRIMARY KEY (`id_category`);

--
-- Indices de la tabla `email_verification`
--
ALTER TABLE `email_verification`
  ADD PRIMARY KEY (`id_email_verification`),
  ADD UNIQUE KEY `UK_rp5nfrbn3mjdq2lhd01nu7ne8` (`email`);

--
-- Indices de la tabla `expenses`
--
ALTER TABLE `expenses`
  ADD PRIMARY KEY (`id_expense`),
  ADD KEY `FKcc5w4ga27lmtjw7yhxndipjn5` (`category`),
  ADD KEY `FKa1y0ufynsa2kpd5q7con93kxl` (`id_user`);

--
-- Indices de la tabla `heritages`
--
ALTER TABLE `heritages`
  ADD PRIMARY KEY (`id_heritage`),
  ADD KEY `FKg9ko06pk3tv1nk5mtpq1wsfon` (`type_heritages`),
  ADD KEY `FKcy2n2wm6nmttqro8qt7ek1wl7` (`id_user`);

--
-- Indices de la tabla `incomes`
--
ALTER TABLE `incomes`
  ADD PRIMARY KEY (`id_income`),
  ADD KEY `FK64cpr5yai4ua2l090x46mvuwh` (`category`),
  ADD KEY `FKfxdymf0y07d041bvm6m5gh9ol` (`id_user`);

--
-- Indices de la tabla `passwordrestoretoken`
--
ALTER TABLE `passwordrestoretoken`
  ADD PRIMARY KEY (`id_restorepassword`),
  ADD UNIQUE KEY `UK_tcxom8sjxv587soxe145gdffc` (`id_user`);

--
-- Indices de la tabla `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`id_profile`),
  ADD UNIQUE KEY `UK9ok7b3w2xt58v028caw3vcwnb` (`document`);

--
-- Indices de la tabla `type_heritages`
--
ALTER TABLE `type_heritages`
  ADD PRIMARY KEY (`id_type_heritage`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UK_rcle35tk5t6py9hf7uow9qkcw` (`id_profile`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `audit_log`
--
ALTER TABLE `audit_log`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `category_expenses`
--
ALTER TABLE `category_expenses`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `category_incomes`
--
ALTER TABLE `category_incomes`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `email_verification`
--
ALTER TABLE `email_verification`
  MODIFY `id_email_verification` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `expenses`
--
ALTER TABLE `expenses`
  MODIFY `id_expense` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `heritages`
--
ALTER TABLE `heritages`
  MODIFY `id_heritage` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `incomes`
--
ALTER TABLE `incomes`
  MODIFY `id_income` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `passwordrestoretoken`
--
ALTER TABLE `passwordrestoretoken`
  MODIFY `id_restorepassword` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `profiles`
--
ALTER TABLE `profiles`
  MODIFY `id_profile` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `type_heritages`
--
ALTER TABLE `type_heritages`
  MODIFY `id_type_heritage` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `expenses`
--
ALTER TABLE `expenses`
  ADD CONSTRAINT `FKa1y0ufynsa2kpd5q7con93kxl` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  ADD CONSTRAINT `FKcc5w4ga27lmtjw7yhxndipjn5` FOREIGN KEY (`category`) REFERENCES `category_expenses` (`id_category`);

--
-- Filtros para la tabla `heritages`
--
ALTER TABLE `heritages`
  ADD CONSTRAINT `FKcy2n2wm6nmttqro8qt7ek1wl7` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  ADD CONSTRAINT `FKg9ko06pk3tv1nk5mtpq1wsfon` FOREIGN KEY (`type_heritages`) REFERENCES `type_heritages` (`id_type_heritage`);

--
-- Filtros para la tabla `incomes`
--
ALTER TABLE `incomes`
  ADD CONSTRAINT `FK64cpr5yai4ua2l090x46mvuwh` FOREIGN KEY (`category`) REFERENCES `category_incomes` (`id_category`),
  ADD CONSTRAINT `FKfxdymf0y07d041bvm6m5gh9ol` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Filtros para la tabla `passwordrestoretoken`
--
ALTER TABLE `passwordrestoretoken`
  ADD CONSTRAINT `FKhsl9jeyi1i6bpwf20fib0vdvx` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Filtros para la tabla `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK9wbwuppwb63kwaj2s6aaqi3th` FOREIGN KEY (`id_profile`) REFERENCES `profiles` (`id_profile`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

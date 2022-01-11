--
-- PostgreSQL database dump
--

-- Dumped from database version 11.12
-- Dumped by pg_dump version 13.3

-- Started on 2022-01-10 11:59:48 -05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 224 (class 1255 OID 78275)
-- Name: createplaceinstitution(integer, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.createplaceinstitution(_total integer, _institutionid bigint) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE contador int:=0;
DECLARE secuencial int:=0;

BEGIN

	while contador < _total loop
		select max(id)+1 into secuencial from place;
		insert into place(id, number, status_id, institution_id)
	  	values(secuencial, (contador+1), 1351, _institutionid);
		--RAISE NOTICE 'contador:%', contador;
	
		contador := contador +1;
	END LOOP;

	return contador;
END
$$;


ALTER FUNCTION public.createplaceinstitution(_total integer, _institutionid bigint) OWNER TO postgres;

--
-- TOC entry 239 (class 1255 OID 78278)
-- Name: createsequenceinstitution(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.createsequenceinstitution(_sequencename character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$ 
declare sequencename character varying;
BEGIN  
	sequencename :=  'CREATE SEQUENCE '||_sequencename||' 
				       start 1
				       INCREMENT BY 1;';
       EXECUTE  sequencename;
	return 1;
END
$$;


ALTER FUNCTION public.createsequenceinstitution(_sequencename character varying) OWNER TO postgres;

--
-- TOC entry 238 (class 1255 OID 78284)
-- Name: getsequencenumber(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getsequencenumber(_sequencename character varying) RETURNS bigint
    LANGUAGE plpgsql
    AS $$ 

DECLARE id bigint:=0;
begin
	select nextval(_sequencename) into id;
	return id;
END
$$;


ALTER FUNCTION public.getsequencenumber(_sequencename character varying) OWNER TO postgres;

--
-- TOC entry 237 (class 1255 OID 78259)
-- Name: placesbyinstitution(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.placesbyinstitution(_institutionid bigint, OUT placeid_ numeric, OUT ticketid_ numeric, OUT number_ character varying, OUT placestatus_ character varying, OUT placecode_ character varying, OUT ticketstatus_ character varying, OUT ticketcode_ character varying, OUT sequential_ character varying, OUT initdate_ date, OUT enddate_ date) RETURNS SETOF record
    LANGUAGE plpgsql
    AS $$  
DECLARE _place record;
DECLARE _record record;
BEGIN
	RAISE NOTICE 'Obteniendo lugares por institucion';
	
	for _place in select p.*, ic.code , ic."name" 
				  from place p  
				  inner join item_catalogue ic on ic.id = p.status_id 
				  where p.institution_id = _institutionid
				  order by cast(p.number as int8)
	loop
		placeid_ 		:= _place.id;
		number_ 		:= _place.number;
		placestatus_ 	:= _place.name;
		placecode_		:= _place.code;
		ticketid_		:= null;
		ticketstatus_	:= null;
		ticketcode_		:= null;
		sequential_		:= null;
		initdate_		:= null;
		enddate_		:= null;
		for _record in select rt.*, ic."name" , ic.code 
					from record_ticket rt
					inner join place p on p.id = rt.placeid_id
					inner join item_catalogue ic on ic.id = rt.status_id 
					where rt.status_id not in (1502, 1503) 
					and p.institution_id = _institutionid
					and p.id = _place.id
		loop
			ticketid_		:= _record.id;
			ticketstatus_	:= _record.name;
			ticketcode_		:= _record.code;
			sequential_		:= _record.sequential;
			initdate_		:= _record.init_date;
			enddate_		:= _record.end_date;
		end loop;

		return next;
	end loop;

END
$$;


ALTER FUNCTION public.placesbyinstitution(_institutionid bigint, OUT placeid_ numeric, OUT ticketid_ numeric, OUT number_ character varying, OUT placestatus_ character varying, OUT placecode_ character varying, OUT ticketstatus_ character varying, OUT ticketcode_ character varying, OUT sequential_ character varying, OUT initdate_ date, OUT enddate_ date) OWNER TO postgres;

SET default_tablespace = '';

--
-- TOC entry 209 (class 1259 OID 61692)
-- Name: authority_functionality; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authority_functionality (
    id bigint NOT NULL,
    authority character varying(255) NOT NULL,
    priority integer NOT NULL,
    active boolean,
    functionality_id bigint NOT NULL
);


ALTER TABLE public.authority_functionality OWNER TO postgres;

--
-- TOC entry 3347 (class 0 OID 0)
-- Dependencies: 209
-- Name: TABLE authority_functionality; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.authority_functionality IS 'The Entity AuthorityFunctionality.\n@author macf';


--
-- TOC entry 3348 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN authority_functionality.authority; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.authority_functionality.authority IS 'ROL de tabla authority';


--
-- TOC entry 3349 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN authority_functionality.priority; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.authority_functionality.priority IS 'prioridad';


--
-- TOC entry 3350 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN authority_functionality.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.authority_functionality.active IS 'activo';


--
-- TOC entry 202 (class 1259 OID 61635)
-- Name: catalogue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.catalogue (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    description character varying(255),
    active boolean
);


ALTER TABLE public.catalogue OWNER TO postgres;

--
-- TOC entry 3351 (class 0 OID 0)
-- Dependencies: 202
-- Name: TABLE catalogue; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.catalogue IS 'The Entity catalog\n@author macf';


--
-- TOC entry 3352 (class 0 OID 0)
-- Dependencies: 202
-- Name: COLUMN catalogue.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.catalogue.name IS 'nombre de catalogo';


--
-- TOC entry 3353 (class 0 OID 0)
-- Dependencies: 202
-- Name: COLUMN catalogue.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.catalogue.code IS 'codigo de catalogo-unico';


--
-- TOC entry 3354 (class 0 OID 0)
-- Dependencies: 202
-- Name: COLUMN catalogue.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.catalogue.description IS 'descripcion de catalogo';


--
-- TOC entry 3355 (class 0 OID 0)
-- Dependencies: 202
-- Name: COLUMN catalogue.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.catalogue.active IS 'activo';


--
-- TOC entry 210 (class 1259 OID 61697)
-- Name: contract; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contract (
    id bigint NOT NULL,
    number character varying(255) NOT NULL,
    init_date date NOT NULL,
    end_date date NOT NULL,
    value numeric(21,2) NOT NULL,
    hours numeric(21,2),
    status_id bigint NOT NULL,
    contractor_id bigint NOT NULL
);


ALTER TABLE public.contract OWNER TO postgres;

--
-- TOC entry 3356 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN contract.number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contract.number IS 'number';


--
-- TOC entry 3357 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN contract.init_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contract.init_date IS 'inicio del contrato';


--
-- TOC entry 3358 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN contract.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contract.end_date IS 'fin del contrato';


--
-- TOC entry 3359 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN contract.value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contract.value IS 'valor del contrato';


--
-- TOC entry 3360 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN contract.hours; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.contract.hours IS 'hours';


--
-- TOC entry 197 (class 1259 OID 61595)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 61590)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 61684)
-- Name: functionality; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.functionality (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    icon character varying(255),
    url character varying(80),
    active boolean NOT NULL,
    parent_id bigint
);


ALTER TABLE public.functionality OWNER TO postgres;

--
-- TOC entry 3361 (class 0 OID 0)
-- Dependencies: 208
-- Name: TABLE functionality; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.functionality IS 'The Functionality entity.\n@author macf';


--
-- TOC entry 3362 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN functionality.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.functionality.name IS 'nombre de la funcionalidad';


--
-- TOC entry 3363 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN functionality.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.functionality.description IS 'descripcion';


--
-- TOC entry 3364 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN functionality.icon; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.functionality.icon IS 'icono para menu';


--
-- TOC entry 3365 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN functionality.url; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.functionality.url IS 'url de la pagina';


--
-- TOC entry 3366 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN functionality.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.functionality.active IS 'activo';


--
-- TOC entry 211 (class 1259 OID 61702)
-- Name: horary; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.horary (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    start_time timestamp without time zone NOT NULL,
    final_hour timestamp without time zone NOT NULL,
    status_id bigint NOT NULL,
    contract_id bigint
);


ALTER TABLE public.horary OWNER TO postgres;

--
-- TOC entry 3367 (class 0 OID 0)
-- Dependencies: 211
-- Name: TABLE horary; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.horary IS 'The Horary entity.\n@author macf';


--
-- TOC entry 3368 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN horary.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.horary.name IS 'name';


--
-- TOC entry 3369 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN horary.start_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.horary.start_time IS 'startTime';


--
-- TOC entry 3370 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN horary.final_hour; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.horary.final_hour IS 'finalHour';


--
-- TOC entry 204 (class 1259 OID 61653)
-- Name: institution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institution (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    address character varying(255),
    places_number integer NOT NULL,
    ruc character varying(255) NOT NULL,
    latitude numeric(21,10),
    longitude numeric(21,10),
    canton_id bigint,
    acronym character varying,
    sequencename character varying(50)
);


ALTER TABLE public.institution OWNER TO postgres;

--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 204
-- Name: TABLE institution; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.institution IS 'The Institution entity.\n@author macf';


--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.name IS 'nombre de la empresa/institucion';


--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.address; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.address IS 'Direccion';


--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.places_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.places_number IS 'espacios disponibles';


--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.ruc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.ruc IS 'ruc de la empresa';


--
-- TOC entry 3376 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.latitude; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.latitude IS 'latitud';


--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 204
-- Name: COLUMN institution.longitude; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.institution.longitude IS 'longitud';


--
-- TOC entry 203 (class 1259 OID 61645)
-- Name: item_catalogue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_catalogue (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    description character varying(255),
    catalog_code character varying(255) NOT NULL,
    active boolean,
    catalogue_id bigint
);


ALTER TABLE public.item_catalogue OWNER TO postgres;

--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE item_catalogue; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.item_catalogue IS 'The Entity entity.\n@author A true hipster';


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN item_catalogue.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_catalogue.name IS 'nombre de itemcatalogo';


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN item_catalogue.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_catalogue.code IS 'codigo de itemcatalogo';


--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN item_catalogue.description; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_catalogue.description IS 'descripcion de itemcatalogo';


--
-- TOC entry 3382 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN item_catalogue.catalog_code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_catalogue.catalog_code IS 'codigo de catalogo';


--
-- TOC entry 3383 (class 0 OID 0)
-- Dependencies: 203
-- Name: COLUMN item_catalogue.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.item_catalogue.active IS 'activo';


--
-- TOC entry 200 (class 1259 OID 61615)
-- Name: jhi_authority; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jhi_authority (
    name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_authority OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 61603)
-- Name: jhi_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jhi_user (
    id bigint NOT NULL,
    login character varying(50) NOT NULL,
    password_hash character varying(60) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(191),
    image_url character varying(256),
    activated boolean NOT NULL,
    lang_key character varying(10),
    activation_key character varying(20),
    reset_key character varying(20),
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone,
    reset_date timestamp without time zone,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone
);


ALTER TABLE public.jhi_user OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 61620)
-- Name: jhi_user_authority; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jhi_user_authority (
    user_id bigint NOT NULL,
    authority_name character varying(50) NOT NULL
);


ALTER TABLE public.jhi_user_authority OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 61676)
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    id bigint NOT NULL,
    full_name character varying(255),
    fist_name character varying(255) NOT NULL,
    surname character varying(255),
    email character varying(255),
    identificaction_number character varying(255) NOT NULL,
    telephone_number character varying(255),
    user_id bigint,
    identification_type_id bigint NOT NULL
);


ALTER TABLE public.person OWNER TO postgres;

--
-- TOC entry 3384 (class 0 OID 0)
-- Dependencies: 207
-- Name: TABLE person; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.person IS 'The Person entity.\n@author macf';


--
-- TOC entry 3385 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.full_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.full_name IS 'nombres completo';


--
-- TOC entry 3386 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.fist_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.fist_name IS 'nombre';


--
-- TOC entry 3387 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.surname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.surname IS 'apellido';


--
-- TOC entry 3388 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.email IS 'email';


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.identificaction_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.identificaction_number IS 'numero identificacion';


--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN person.telephone_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.person.telephone_number IS 'numero telefono convencional';


--
-- TOC entry 218 (class 1259 OID 61875)
-- Name: place; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.place (
    id bigint NOT NULL,
    number character varying(255) NOT NULL,
    status_id bigint,
    institution_id bigint
);


ALTER TABLE public.place OWNER TO postgres;

--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE place; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.place IS 'The Place entity.\n@author A true hipster';


--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN place.number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.place.number IS 'number';


--
-- TOC entry 216 (class 1259 OID 61732)
-- Name: receipt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.receipt (
    id bigint NOT NULL,
    authorization_number character varying(255),
    sequential character varying(255),
    status character varying(255),
    sriaccesskey character varying(255),
    sriauthorizationdate date,
    receiptdate date,
    recordticketid_id bigint
);


ALTER TABLE public.receipt OWNER TO postgres;

--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE receipt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.receipt IS 'The Receipt entity.\n@author A true hipster';


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.authorization_number; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.authorization_number IS 'autorizacion del SRI';


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.sequential; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.sequential IS 'secuencia de la factura';


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.status IS 'estado de la factura';


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.sriaccesskey; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.sriaccesskey IS 'clave de acceso sri';


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.sriauthorizationdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.sriauthorizationdate IS 'fecha autorizacion sri';


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN receipt.receiptdate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.receipt.receiptdate IS 'fecha emision factura';


--
-- TOC entry 219 (class 1259 OID 61900)
-- Name: record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.record (
    id bigint NOT NULL
);


ALTER TABLE public.record OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 61742)
-- Name: record_ticket; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.record_ticket (
    id bigint NOT NULL,
    init_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone,
    plate character varying(255),
    parking_time timestamp without time zone,
    taxable_total numeric(21,2),
    taxes numeric(21,2),
    total numeric(21,2),
    status_id bigint NOT NULL,
    tariff_vehicle_id bigint NOT NULL,
    institution_id bigint NOT NULL,
    observation character varying,
    placeid_id bigint,
    sequential character varying,
    emitter_id bigint,
    collector_id bigint,
    reserver_id bigint
);


ALTER TABLE public.record_ticket OWNER TO postgres;

--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE record_ticket; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.record_ticket IS 'The Entity RecordTicket.\n@author Renmacfe';


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.init_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.init_date IS 'fecha inicio de ingreso';


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.end_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.end_date IS 'fecha fin de salida';


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.plate; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.plate IS 'placa';


--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.parking_time; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.parking_time IS 'tiempo de parqueo';


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.taxable_total; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.taxable_total IS 'subtotal a pagar';


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.taxes; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.taxes IS 'iva a pagar';


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN record_ticket.total; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.record_ticket.total IS 'iva a pagar';


--
-- TOC entry 223 (class 1259 OID 78315)
-- Name: seq_demoparis; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_demoparis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_demoparis OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 78302)
-- Name: seq_parqueaderosancayetano; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_parqueaderosancayetano
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_parqueaderosancayetano OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 78304)
-- Name: seq_sancayetano; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.seq_sancayetano
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_sancayetano OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 61601)
-- Name: sequence_generator; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sequence_generator
    START WITH 1050
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_generator OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 70067)
-- Name: sequence_loja_bolivar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sequence_loja_bolivar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_loja_bolivar OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 61671)
-- Name: system_parameter_institution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.system_parameter_institution (
    id bigint NOT NULL,
    value character varying(255) NOT NULL,
    active boolean,
    parameter_id bigint NOT NULL,
    institution_id bigint NOT NULL
);


ALTER TABLE public.system_parameter_institution OWNER TO postgres;

--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 206
-- Name: TABLE system_parameter_institution; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.system_parameter_institution IS 'The Entity SystemParameterInstitution.\n@author macf';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN system_parameter_institution.value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_parameter_institution.value IS 'valor del campo por empresa';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN system_parameter_institution.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_parameter_institution.active IS 'determinar si esta activo o no el parametro por empresa';


--
-- TOC entry 205 (class 1259 OID 61661)
-- Name: system_parameters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.system_parameters (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    code character varying(255) NOT NULL,
    clase character varying(255) NOT NULL
);


ALTER TABLE public.system_parameters OWNER TO postgres;

--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 205
-- Name: TABLE system_parameters; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.system_parameters IS 'The Entity SystemParameters.\n@author macf';


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN system_parameters.name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_parameters.name IS 'nombre';


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN system_parameters.code; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_parameters.code IS 'codigo';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN system_parameters.clase; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.system_parameters.clase IS 'clase de java';


--
-- TOC entry 212 (class 1259 OID 61712)
-- Name: tariff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tariff (
    id bigint NOT NULL,
    initial_date date NOT NULL,
    final_date date,
    institution_id bigint NOT NULL
);


ALTER TABLE public.tariff OWNER TO postgres;

--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 212
-- Name: TABLE tariff; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tariff IS 'The Tariff entity.\n@author Rene';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN tariff.initial_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tariff.initial_date IS 'fecha inicial de tarifa';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN tariff.final_date; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tariff.final_date IS 'fecha final de tarifa';


--
-- TOC entry 213 (class 1259 OID 61717)
-- Name: tariff_vehicle_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tariff_vehicle_type (
    id bigint NOT NULL,
    min_value integer NOT NULL,
    max_value integer NOT NULL,
    value numeric(21,2) NOT NULL,
    vehicle_type_id bigint NOT NULL,
    tariff_id bigint
);


ALTER TABLE public.tariff_vehicle_type OWNER TO postgres;

--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 213
-- Name: TABLE tariff_vehicle_type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.tariff_vehicle_type IS 'The TariffVehicleType entity.\n@author macf';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN tariff_vehicle_type.min_value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tariff_vehicle_type.min_value IS 'valor minimo';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN tariff_vehicle_type.max_value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tariff_vehicle_type.max_value IS 'valor maximo';


--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN tariff_vehicle_type.value; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tariff_vehicle_type.value IS 'valor tarifa';


--
-- TOC entry 214 (class 1259 OID 61722)
-- Name: user_authority; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_authority (
    id bigint NOT NULL,
    authority character varying(255),
    active boolean,
    user_id bigint
);


ALTER TABLE public.user_authority OWNER TO postgres;

--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 214
-- Name: TABLE user_authority; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_authority IS 'The UserAuthority entity.\n@author macf';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 214
-- Name: COLUMN user_authority.authority; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_authority.authority IS 'authority';


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 214
-- Name: COLUMN user_authority.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_authority.active IS 'activo';


--
-- TOC entry 215 (class 1259 OID 61727)
-- Name: user_authority_institution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_authority_institution (
    id bigint NOT NULL,
    active boolean,
    institution_id bigint,
    user_authority_id bigint
);


ALTER TABLE public.user_authority_institution OWNER TO postgres;

--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE user_authority_institution; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.user_authority_institution IS 'The UserAuthorityInstitution entity.\n@author macf';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN user_authority_institution.active; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.user_authority_institution.active IS 'activo';


--
-- TOC entry 3327 (class 0 OID 61692)
-- Dependencies: 209
-- Data for Name: authority_functionality; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3320 (class 0 OID 61635)
-- Dependencies: 202
-- Data for Name: catalogue; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.catalogue (id, name, code, description, active) VALUES (1001, 'CANTON', 'CANTON', 'CANTON', true);
INSERT INTO public.catalogue (id, name, code, description, active) VALUES (1002, 'TIPOS DE VEHICULOS', 'VEHICLES_TYPES', 'TIPOS DE VEHICULOS', true);
INSERT INTO public.catalogue (id, name, code, description, active) VALUES (1301, 'ESTADO DE LUGAR DE PARQUEAMIENTO', 'STATUS_PLACE', 'ESTADO DEL LUGAR DE PARQUEO', true);
INSERT INTO public.catalogue (id, name, code, description, active) VALUES (1451, 'ESTADO DE TICKET', 'TICKET_STATUS', 'ESTADO DEL TICKET', true);


--
-- TOC entry 3328 (class 0 OID 61697)
-- Dependencies: 210
-- Data for Name: contract; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3315 (class 0 OID 61595)
-- Dependencies: 197
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225535-1-data', 'jhipster', 'config/liquibase/changelog/20211019225535_added_entity_Institution.xml', '2021-10-22 05:58:02.936683', 8, 'EXECUTED', '8:2fe654e8f26c750da3669fe1c613d68e', 'loadData tableName=institution', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225547-1', 'jhipster', 'config/liquibase/changelog/20211019225547_added_entity_UserAuthorityInstitution.xml', '2021-10-22 05:58:04.507402', 31, 'EXECUTED', '8:d530ec86e100ddfa66229135fd55c7c1', 'createTable tableName=user_authority_institution', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225547-2', 'jhipster', 'config/liquibase/changelog/20211019225547_added_entity_constraints_UserAuthorityInstitution.xml', '2021-10-22 05:58:06.012708', 48, 'EXECUTED', '8:ace3328490a448c5f432f2db6194fa23', 'addForeignKeyConstraint baseTableName=user_authority_institution, constraintName=fk_user_authority_institution__institution_id, referencedTableName=institution; addForeignKeyConstraint baseTableName=user_authority_institution, constraintName=fk_us...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211027213911-1-data', 'jhipster', 'config/liquibase/changelog/20211027213911_added_entity_Place.xml', '2021-10-27 17:08:50.380656', 53, 'EXECUTED', '8:a51d754f816af5fddb0a3d6b4ffbe400', 'loadData tableName=place', '', NULL, '4.5.0', 'faker', NULL, '5372530118');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225534-1', 'jhipster', 'config/liquibase/changelog/20211019225534_added_entity_ItemCatalogue.xml', '2021-10-22 05:58:02.834945', 5, 'EXECUTED', '8:2e726dbeb9e50c89049bfcba85368212', 'createTable tableName=item_catalogue', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225534-1-data', 'jhipster', 'config/liquibase/changelog/20211019225534_added_entity_ItemCatalogue.xml', '2021-10-22 05:58:02.875462', 6, 'EXECUTED', '8:f1229237b30257901da0be2503f64631', 'loadData tableName=item_catalogue', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225535-1', 'jhipster', 'config/liquibase/changelog/20211019225535_added_entity_Institution.xml', '2021-10-22 05:58:02.893838', 7, 'EXECUTED', '8:dbd04b8c63d39f59ef55b18eeb7ede34', 'createTable tableName=institution', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225533-1', 'jhipster', 'config/liquibase/changelog/20211019225533_added_entity_Catalogue.xml', '2021-10-22 05:58:02.78816', 3, 'EXECUTED', '8:95845c3e08f10e5f4e91209531fcac1f', 'createTable tableName=catalogue', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225539-2', 'jhipster', 'config/liquibase/changelog/20211019225539_added_entity_constraints_Functionality.xml', '2021-10-22 05:58:05.385798', 40, 'EXECUTED', '8:9500587033edab9e7b65134907886898', 'addForeignKeyConstraint baseTableName=functionality, constraintName=fk_functionality__parent_id, referencedTableName=functionality', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225540-2', 'jhipster', 'config/liquibase/changelog/20211019225540_added_entity_constraints_AuthorityFunctionality.xml', '2021-10-22 05:58:05.873079', 41, 'EXECUTED', '8:3cdcaefc2419eadc30bde1ec6bc45a66', 'addForeignKeyConstraint baseTableName=authority_functionality, constraintName=fk_authority_functionality__functionality_id, referencedTableName=functionality', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225541-2', 'jhipster', 'config/liquibase/changelog/20211019225541_added_entity_constraints_Contract.xml', '2021-10-22 05:58:05.919552', 42, 'EXECUTED', '8:b42ff677bdd91364e5fe1095fc50e630', 'addForeignKeyConstraint baseTableName=contract, constraintName=fk_contract__status_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=contract, constraintName=fk_contract__contractor_id, referencedTableName=person', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225542-2', 'jhipster', 'config/liquibase/changelog/20211019225542_added_entity_constraints_Horary.xml', '2021-10-22 05:58:05.927073', 43, 'EXECUTED', '8:f5076b27cf2749f638fc86c5f72470b3', 'addForeignKeyConstraint baseTableName=horary, constraintName=fk_horary__status_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=horary, constraintName=fk_horary__contract_id, referencedTableName=contract', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225544-2', 'jhipster', 'config/liquibase/changelog/20211019225544_added_entity_constraints_Tariff.xml', '2021-10-22 05:58:05.946731', 45, 'EXECUTED', '8:8fb9a2fc876bb240fdfa5212911ceff4', 'addForeignKeyConstraint baseTableName=tariff, constraintName=fk_tariff__institution_id, referencedTableName=institution', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225545-2', 'jhipster', 'config/liquibase/changelog/20211019225545_added_entity_constraints_TariffVehicleType.xml', '2021-10-22 05:58:05.955494', 46, 'EXECUTED', '8:5da462fe0d67138195219fd1b52859e9', 'addForeignKeyConstraint baseTableName=tariff_vehicle_type, constraintName=fk_tariff_vehicle_type__vehicle_type_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=tariff_vehicle_type, constraintName=fk_tariff_vehicle_type...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2021-10-22 05:58:02.733763', 2, 'EXECUTED', '8:bd2c609d6590a94c79b0ef226f5207c5', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225536-1-data', 'jhipster', 'config/liquibase/changelog/20211019225536_added_entity_SystemParameters.xml', '2021-10-22 05:58:02.983449', 10, 'EXECUTED', '8:1c09426344436ec509bf1c04459f7599', 'loadData tableName=system_parameters', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225540-1', 'jhipster', 'config/liquibase/changelog/20211019225540_added_entity_AuthorityFunctionality.xml', '2021-10-22 05:58:03.591986', 17, 'EXECUTED', '8:06e3256eb7995675c92e30cd30026624', 'createTable tableName=authority_functionality', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225546-2', 'jhipster', 'config/liquibase/changelog/20211019225546_added_entity_constraints_UserAuthority.xml', '2021-10-22 05:58:05.962208', 47, 'EXECUTED', '8:fb8cd36eed766d92bdda7c458c2e61be', 'addForeignKeyConstraint baseTableName=user_authority, constraintName=fk_user_authority__user_id, referencedTableName=jhi_user', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225537-1', 'jhipster', 'config/liquibase/changelog/20211019225537_added_entity_SystemParameterInstitution.xml', '2021-10-22 05:58:02.997999', 11, 'EXECUTED', '8:e91bf56d5c0305dedbfcde0f353bffa6', 'createTable tableName=system_parameter_institution', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225533-1-data', 'jhipster', 'config/liquibase/changelog/20211019225533_added_entity_Catalogue.xml', '2021-10-22 05:58:02.819321', 4, 'EXECUTED', '8:6b4b61c6a5a73ce10886d339c175b187', 'loadData tableName=catalogue', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225536-1', 'jhipster', 'config/liquibase/changelog/20211019225536_added_entity_SystemParameters.xml', '2021-10-22 05:58:02.951774', 9, 'EXECUTED', '8:82f4bff50bdd405ead98e7dba44484ed', 'createTable tableName=system_parameters', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225537-1-data', 'jhipster', 'config/liquibase/changelog/20211019225537_added_entity_SystemParameterInstitution.xml', '2021-10-22 05:58:03.031987', 12, 'EXECUTED', '8:87842ac7eadb05ff669c6dccb5eb60be', 'loadData tableName=system_parameter_institution', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225538-1', 'jhipster', 'config/liquibase/changelog/20211019225538_added_entity_Person.xml', '2021-10-22 05:58:03.086849', 13, 'EXECUTED', '8:165e9624c28842e0efb738e12b23274d', 'createTable tableName=person', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225538-1-data', 'jhipster', 'config/liquibase/changelog/20211019225538_added_entity_Person.xml', '2021-10-22 05:58:03.210073', 14, 'EXECUTED', '8:e702d8ccbf008f3f8439308730b4a564', 'loadData tableName=person', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225539-1', 'jhipster', 'config/liquibase/changelog/20211019225539_added_entity_Functionality.xml', '2021-10-22 05:58:03.502295', 15, 'EXECUTED', '8:334d88141c3eb81cf1fcd222b29c8e9c', 'createTable tableName=functionality', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225539-1-data', 'jhipster', 'config/liquibase/changelog/20211019225539_added_entity_Functionality.xml', '2021-10-22 05:58:03.575469', 16, 'EXECUTED', '8:b54f72bb18bd8d3f7232a5509effc011', 'loadData tableName=functionality', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225541-1', 'jhipster', 'config/liquibase/changelog/20211019225541_added_entity_Contract.xml', '2021-10-22 05:58:03.713688', 19, 'EXECUTED', '8:d8de5e82e27dc19ee499e5acc26fb925', 'createTable tableName=contract', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225541-1-data', 'jhipster', 'config/liquibase/changelog/20211019225541_added_entity_Contract.xml', '2021-10-22 05:58:03.789497', 20, 'EXECUTED', '8:25e6547f7a34808507bf3455e7c907c1', 'loadData tableName=contract', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225542-1', 'jhipster', 'config/liquibase/changelog/20211019225542_added_entity_Horary.xml', '2021-10-22 05:58:03.798937', 21, 'EXECUTED', '8:b1a06015a14f7377ff01901c485fafb9', 'createTable tableName=horary; dropDefaultValue columnName=start_time, tableName=horary; dropDefaultValue columnName=final_hour, tableName=horary', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225542-1-data', 'jhipster', 'config/liquibase/changelog/20211019225542_added_entity_Horary.xml', '2021-10-22 05:58:03.846597', 22, 'EXECUTED', '8:dc604d08caba0ad86d147fd04b352de9', 'loadData tableName=horary', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225543-1-data', 'jhipster', 'config/liquibase/changelog/20211019225543_added_entity_Record.xml', '2021-10-22 05:58:03.926835', 24, 'EXECUTED', '8:0d2f49705ecd150d20ed0024a6516606', 'loadData tableName=record', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225544-1', 'jhipster', 'config/liquibase/changelog/20211019225544_added_entity_Tariff.xml', '2021-10-22 05:58:03.938049', 25, 'EXECUTED', '8:b4aa0c1fc5ad6665d7c5480574ac0e7d', 'createTable tableName=tariff', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225545-1', 'jhipster', 'config/liquibase/changelog/20211019225545_added_entity_TariffVehicleType.xml', '2021-10-22 05:58:04.345531', 27, 'EXECUTED', '8:10d184ef85c8a04328c1f726579ec4e0', 'createTable tableName=tariff_vehicle_type', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225545-1-data', 'jhipster', 'config/liquibase/changelog/20211019225545_added_entity_TariffVehicleType.xml', '2021-10-22 05:58:04.38046', 28, 'EXECUTED', '8:3dca712e7092222d0a1f8830986c3a9e', 'loadData tableName=tariff_vehicle_type', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225546-1', 'jhipster', 'config/liquibase/changelog/20211019225546_added_entity_UserAuthority.xml', '2021-10-22 05:58:04.390772', 29, 'EXECUTED', '8:e29ba4268d340c503fbe4efd95ef1dea', 'createTable tableName=user_authority', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225546-1-data', 'jhipster', 'config/liquibase/changelog/20211019225546_added_entity_UserAuthority.xml', '2021-10-22 05:58:04.418415', 30, 'EXECUTED', '8:907f244a06390a0c719b5213cc7a4fbd', 'loadData tableName=user_authority', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225548-1', 'jhipster', 'config/liquibase/changelog/20211019225548_added_entity_Receipt.xml', '2021-10-22 05:58:04.569648', 33, 'EXECUTED', '8:4d36e869c5a4e84989a6d2e3256e0433', 'createTable tableName=receipt', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225548-1-data', 'jhipster', 'config/liquibase/changelog/20211019225548_added_entity_Receipt.xml', '2021-10-22 05:58:04.61448', 34, 'EXECUTED', '8:c5f52e01e710b628c2c161087ea720a0', 'loadData tableName=receipt', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225534-2', 'jhipster', 'config/liquibase/changelog/20211019225534_added_entity_constraints_ItemCatalogue.xml', '2021-10-22 05:58:05.282681', 37, 'EXECUTED', '8:276842fef1414a592ca2969bace486d1', 'addForeignKeyConstraint baseTableName=item_catalogue, constraintName=fk_item_catalogue__catalogue_id, referencedTableName=catalogue', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225537-2', 'jhipster', 'config/liquibase/changelog/20211019225537_added_entity_constraints_SystemParameterInstitution.xml', '2021-10-22 05:58:05.329064', 38, 'EXECUTED', '8:b778a28a7b32e175f4b234bba36ed544', 'addForeignKeyConstraint baseTableName=system_parameter_institution, constraintName=fk_system_parameter_institution__parameter_id, referencedTableName=system_parameters; addForeignKeyConstraint baseTableName=system_parameter_institution, constraint...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225538-2', 'jhipster', 'config/liquibase/changelog/20211019225538_added_entity_constraints_Person.xml', '2021-10-22 05:58:05.339507', 39, 'EXECUTED', '8:311c6156279c8baa69eb227d20c1a3e6', 'addForeignKeyConstraint baseTableName=person, constraintName=fk_person__user_id, referencedTableName=jhi_user; addForeignKeyConstraint baseTableName=person, constraintName=fk_person__identification_type_id, referencedTableName=item_catalogue', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211027213911-1', 'jhipster', 'config/liquibase/changelog/20211027213911_added_entity_Place.xml', '2021-10-27 17:08:50.303397', 52, 'EXECUTED', '8:3c590041e93653893f348071d01521b0', 'createTable tableName=place', '', NULL, '4.5.0', NULL, NULL, '5372530118');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211027213911-2', 'jhipster', 'config/liquibase/changelog/20211027213911_added_entity_constraints_Place.xml', '2021-10-27 17:08:50.405892', 54, 'EXECUTED', '8:8c9043ec1cf946f9e8af0af3ec84d371', 'addForeignKeyConstraint baseTableName=place, constraintName=fk_place__status_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=place, constraintName=fk_place__institution_id, referencedTableName=institution', '', NULL, '4.5.0', NULL, NULL, '5372530118');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('00000000000000', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2021-10-22 05:58:01.588811', 1, 'EXECUTED', '8:b8c27d9dc8db18b5de87cdb8c38a416b', 'createSequence sequenceName=sequence_generator', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225540-1-data', 'jhipster', 'config/liquibase/changelog/20211019225540_added_entity_AuthorityFunctionality.xml', '2021-10-22 05:58:03.664139', 18, 'EXECUTED', '8:5b44380f96c3252a8c23b3b0ccb011ec', 'loadData tableName=authority_functionality', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225543-1', 'jhipster', 'config/liquibase/changelog/20211019225543_added_entity_Record.xml', '2021-10-22 05:58:03.867172', 23, 'EXECUTED', '8:985cc50145096ba9b44963d825795cb4', 'createTable tableName=record; dropDefaultValue columnName=init_date, tableName=record; dropDefaultValue columnName=end_date, tableName=record; dropDefaultValue columnName=parking_time, tableName=record', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225544-1-data', 'jhipster', 'config/liquibase/changelog/20211019225544_added_entity_Tariff.xml', '2021-10-22 05:58:04.0528', 26, 'EXECUTED', '8:e2252d0186171a5ae94c63f811f7ac7e', 'loadData tableName=tariff', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225535-2', 'jhipster', 'config/liquibase/changelog/20211019225535_added_entity_constraints_Institution.xml', '2021-10-22 05:58:06.240084', 51, 'EXECUTED', '8:4c720b35d6696421d1e60b7abb9eb88b', 'addForeignKeyConstraint baseTableName=institution, constraintName=fk_institution__canton_id, referencedTableName=item_catalogue', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211026021057-1', 'jhipster', 'config/liquibase/changelog/20211026021057_added_entity_Record.xml', '2021-10-27 21:27:42.352427', 55, 'EXECUTED', '8:4239e331f815e06a139d50dbfa81a9a4', 'createTable tableName=record', '', NULL, '4.5.0', NULL, NULL, '5388062187');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225547-1-data', 'jhipster', 'config/liquibase/changelog/20211019225547_added_entity_UserAuthorityInstitution.xml', '2021-10-22 05:58:04.525024', 32, 'EXECUTED', '8:482c98391f94edbab7ba48ae4264554b', 'loadData tableName=user_authority_institution', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211020030411-1', 'jhipster', 'config/liquibase/changelog/20211020030411_added_entity_RecordTicket.xml', '2021-10-22 05:58:04.657161', 35, 'EXECUTED', '8:a36de846f4fe706c669d536f1c0183f7', 'createTable tableName=record_ticket; dropDefaultValue columnName=init_date, tableName=record_ticket; dropDefaultValue columnName=end_date, tableName=record_ticket; dropDefaultValue columnName=parking_time, tableName=record_ticket', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211020030411-1-data', 'jhipster', 'config/liquibase/changelog/20211020030411_added_entity_RecordTicket.xml', '2021-10-22 05:58:04.756895', 36, 'EXECUTED', '8:78c203e6989d74922531b811a851538a', 'loadData tableName=record_ticket', '', NULL, '4.5.0', 'faker', NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225543-2', 'jhipster', 'config/liquibase/changelog/20211019225543_added_entity_constraints_Record.xml', '2021-10-22 05:58:05.940316', 44, 'EXECUTED', '8:fdc0efc53c0dfbfb24b80502bc900b2c', 'addForeignKeyConstraint baseTableName=record, constraintName=fk_record__status_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=record, constraintName=fk_record__tariff_vehicle_id, referencedTableName=tariff_vehicle_ty...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211019225548-2', 'jhipster', 'config/liquibase/changelog/20211019225548_added_entity_constraints_Receipt.xml', '2021-10-22 05:58:06.220596', 49, 'EXECUTED', '8:f4a94f20c97318c32cb38ac6cae35ace', 'addForeignKeyConstraint baseTableName=receipt, constraintName=fk_receipt__recordticketid_id, referencedTableName=record_ticket', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211020030411-2', 'jhipster', 'config/liquibase/changelog/20211020030411_added_entity_constraints_RecordTicket.xml', '2021-10-22 05:58:06.232367', 50, 'EXECUTED', '8:05e678045bc198c5fc9afd1a8ed03785', 'addForeignKeyConstraint baseTableName=record_ticket, constraintName=fk_record_ticket__status_id, referencedTableName=item_catalogue; addForeignKeyConstraint baseTableName=record_ticket, constraintName=fk_record_ticket__tariff_vehicle_id, reference...', '', NULL, '4.5.0', NULL, NULL, '4900280962');
INSERT INTO public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) VALUES ('20211026021057-1-data', 'jhipster', 'config/liquibase/changelog/20211026021057_added_entity_Record.xml', '2021-10-27 21:27:42.410844', 56, 'EXECUTED', '8:0d2f49705ecd150d20ed0024a6516606', 'loadData tableName=record', '', NULL, '4.5.0', 'faker', NULL, '5388062187');


--
-- TOC entry 3314 (class 0 OID 61590)
-- Dependencies: 196
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangeloglock (id, locked, lockgranted, lockedby) VALUES (1, false, NULL, NULL);


--
-- TOC entry 3326 (class 0 OID 61684)
-- Dependencies: 208
-- Data for Name: functionality; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3329 (class 0 OID 61702)
-- Dependencies: 211
-- Data for Name: horary; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3322 (class 0 OID 61653)
-- Dependencies: 204
-- Data for Name: institution; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.institution (id, name, address, places_number, ruc, latitude, longitude, canton_id, acronym, sequencename) VALUES (1, 'Parqueadero Público Francisco Javier', 'SUCRE', 30, '123456789', -4.0000000000, -79.2000000000, 1051, NULL, NULL);
INSERT INTO public.institution (id, name, address, places_number, ruc, latitude, longitude, canton_id, acronym, sequencename) VALUES (2, 'Parqueadero Bolivar', 'BOLIVAR', 40, '123456789', -3.9936949000, -3.9936949000, 1051, 'PB', 'sequence_loja_bolivar');
INSERT INTO public.institution (id, name, address, places_number, ruc, latitude, longitude, canton_id, acronym, sequencename) VALUES (4205, 'San Cayetano', 'Paris y Via Zamora', 30, '123456789', -3.9846433000, -79.1974163000, 1051, 'SC', 'seq_sancayetano');
INSERT INTO public.institution (id, name, address, places_number, ruc, latitude, longitude, canton_id, acronym, sequencename) VALUES (5751, 'DEMO PARIS', 'SASD', 10, '1234567890', -3.9851282154, -79.1958510936, 1051, 'DP', 'seq_demoparis');


--
-- TOC entry 3321 (class 0 OID 61645)
-- Dependencies: 203
-- Data for Name: item_catalogue; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1051, 'LOJA', 'LOXA', 'LOJA', 'CANTON', true, 1001);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1052, 'LIVIANO', 'LIGHTWEIGHT', 'TIPO DE VEHICULO LIVIANO', 'VEHICLE_TYPE', true, 1002);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1053, 'PESADOS', 'HEAVY', 'TIPOS DE VEHÍCULOS PESADOS', 'VEHICLE_TYPE', true, 1002);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1351, 'DISPONIBLE', 'FREE_PLACE', 'LUGAR DISPONIBLE', 'STATUS_PLACE', true, 1301);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1352, 'OCUPADO', 'BUSY_PLACE', 'LUGAR OCUPADO', 'STATUS_PLACE', true, 1301);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1353, 'DESHABILITADO', 'DISABLE_PLACE', 'LUGAR DESHABILITADO DEL PARQUEADERO', 'STATUS_PLACE', true, 1301);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1502, 'PAGADO', 'PAYED_TICKET', 'TICKET PAGADO', 'TICKET_STATUS', true, 1451);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1501, 'EMITIDO', 'EMITTED_TICKET', 'TICKET EMITIDO', 'TICKET_STATUS', true, 1451);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (1503, 'ANULADO', 'VOID_TICKET', 'TICKET ANULADO', 'TICKET_STATUS', true, 1451);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (4901, 'PRE EMITIDO', 'PREEMITTED_TICKET', 'TICKET PREEMITIDO', 'TICKET_STATUS', true, 1451);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (5051, 'RESERVADO', 'RESERVED_PLACE', 'LUGAR RESERVADO', 'STATUS_PLACE', false, 1301);
INSERT INTO public.item_catalogue (id, name, code, description, catalog_code, active, catalogue_id) VALUES (5201, 'DENEGADO', 'DENY_TICKET', NULL, 'TICKET_STATUS', true, 1451);


--
-- TOC entry 3318 (class 0 OID 61615)
-- Dependencies: 200
-- Data for Name: jhi_authority; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.jhi_authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO public.jhi_authority (name) VALUES ('ROLE_USER');
INSERT INTO public.jhi_authority (name) VALUES ('ROLE_CASHIER');


--
-- TOC entry 3317 (class 0 OID 61603)
-- Dependencies: 199
-- Data for Name: jhi_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', true, 'es', NULL, NULL, 'system', NULL, NULL, 'system', NULL);
INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', true, 'es', NULL, NULL, 'system', NULL, NULL, 'system', NULL);
INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (1101, 'cajero', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'CAJERO', 'CAJERO', 'cajero@gmail.com', NULL, true, 'es', NULL, '1mijEUfiKdG3k7mCXhGi', 'admin', '2021-10-24 16:58:22.116378', '2021-10-24 16:58:21.719537', 'admin', '2021-10-24 16:58:22.116378');
INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (4252, 'adminbyron', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Byron', 'Alvarez', 'byalvarez1@gmail.com', NULL, true, 'es', NULL, 'QJ8ulKK13CcTGJIeFKPE', 'admin', '2021-12-08 12:32:38.38613', '2021-12-08 12:32:38.383349', 'admin', '2021-12-08 12:32:38.38613');
INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (5851, 'demo', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Demo', 'Demo', 'macartuche@gmail.com', NULL, true, 'es', NULL, 'EDjVCMa9jmRdLfvKjV0w', 'admin', '2022-01-10 03:45:36.325318', '2022-01-10 04:56:37.964865', 'anonymousUser', '2022-01-10 04:56:37.974117');


--
-- TOC entry 3319 (class 0 OID 61620)
-- Dependencies: 201
-- Data for Name: jhi_user_authority; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (1101, 'ROLE_CASHIER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (4252, 'ROLE_CASHIER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (5851, 'ROLE_CASHIER');


--
-- TOC entry 3325 (class 0 OID 61676)
-- Dependencies: 207
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3336 (class 0 OID 61875)
-- Dependencies: 218
-- Data for Name: place; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1451, '21', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1452, '22', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1453, '23', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1454, '24', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1455, '25', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1410, '20', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1411, '21', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1412, '22', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1413, '23', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1414, '24', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1415, '25', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1416, '26', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1417, '27', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1418, '28', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1419, '29', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1420, '30', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1421, '31', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1422, '32', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1423, '33', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1424, '34', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1425, '35', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1426, '36', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1427, '37', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1428, '38', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1429, '39', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1430, '40', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1456, '26', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1457, '27', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1458, '28', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1408, '18', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1459, '29', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1463, '3', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1464, '4', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1405, '15', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1465, '5', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1466, '6', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1467, '7', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1409, '19', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1407, '17', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1468, '8', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1406, '16', 1351, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1469, '9', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1470, '10', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1432, '2', 5051, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1461, '1', 1351, 5751);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1460, '30', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1435, '5', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1436, '6', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1437, '7', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1, '1', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (2, '2', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (3, '3', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (4, '4', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (5, '5', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (6, '6', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (7, '7', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (8, '8', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (9, '9', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (10, '10', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1401, '11', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1402, '12', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1403, '13', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1404, '14', 1352, 2);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1438, '8', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1439, '9', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1433, '3', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1434, '4', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1442, '12', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1443, '13', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1444, '14', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1445, '15', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1446, '16', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1440, '10', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1441, '11', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1447, '17', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1448, '18', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1449, '19', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1450, '20', 1351, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1431, '1', 1352, 4205);
INSERT INTO public.place (id, number, status_id, institution_id) VALUES (1462, '2', 1351, 5751);


--
-- TOC entry 3334 (class 0 OID 61732)
-- Dependencies: 216
-- Data for Name: receipt; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3337 (class 0 OID 61900)
-- Dependencies: 219
-- Data for Name: record; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.record (id) VALUES (1);
INSERT INTO public.record (id) VALUES (2);
INSERT INTO public.record (id) VALUES (3);
INSERT INTO public.record (id) VALUES (4);
INSERT INTO public.record (id) VALUES (5);
INSERT INTO public.record (id) VALUES (6);
INSERT INTO public.record (id) VALUES (7);
INSERT INTO public.record (id) VALUES (8);
INSERT INTO public.record (id) VALUES (9);
INSERT INTO public.record (id) VALUES (10);


--
-- TOC entry 3335 (class 0 OID 61742)
-- Dependencies: 217
-- Data for Name: record_ticket; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.record_ticket (id, init_date, end_date, plate, parking_time, taxable_total, taxes, total, status_id, tariff_vehicle_id, institution_id, observation, placeid_id, sequential, emitter_id, collector_id, reserver_id) VALUES (5652, '2022-01-09 23:12:51.19507', NULL, NULL, NULL, 0.00, 0.00, 0.00, 1501, 4452, 4205, NULL, 1431, 'SC00060', 4252, NULL, 1);
INSERT INTO public.record_ticket (id, init_date, end_date, plate, parking_time, taxable_total, taxes, total, status_id, tariff_vehicle_id, institution_id, observation, placeid_id, sequential, emitter_id, collector_id, reserver_id) VALUES (6003, '2022-01-10 04:21:12.838193', NULL, NULL, NULL, NULL, NULL, NULL, 4901, 4452, 4205, NULL, 1432, 'SC00061', NULL, NULL, 1);
INSERT INTO public.record_ticket (id, init_date, end_date, plate, parking_time, taxable_total, taxes, total, status_id, tariff_vehicle_id, institution_id, observation, placeid_id, sequential, emitter_id, collector_id, reserver_id) VALUES (6002, '2022-01-10 04:25:44.607659', '2022-01-10 04:25:40.845', NULL, '1970-01-01 00:05:47.625', 0.75, 0.06, 0.81, 5201, 6101, 5751, NULL, 1461, 'DP00003', 5851, NULL, 1);


--
-- TOC entry 3324 (class 0 OID 61671)
-- Dependencies: 206
-- Data for Name: system_parameter_institution; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3323 (class 0 OID 61661)
-- Dependencies: 205
-- Data for Name: system_parameters; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.system_parameters (id, name, code, clase) VALUES (5701, 'RADIO', 'RADIO', 'java.lang.Long');


--
-- TOC entry 3330 (class 0 OID 61712)
-- Dependencies: 212
-- Data for Name: tariff; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tariff (id, initial_date, final_date, institution_id) VALUES (10, '2021-10-01', '2025-10-04', 2);
INSERT INTO public.tariff (id, initial_date, final_date, institution_id) VALUES (4402, '2021-12-01', '2022-01-31', 4205);
INSERT INTO public.tariff (id, initial_date, final_date, institution_id) VALUES (6051, '2022-01-01', '2022-01-31', 5751);


--
-- TOC entry 3331 (class 0 OID 61717)
-- Dependencies: 213
-- Data for Name: tariff_vehicle_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tariff_vehicle_type (id, min_value, max_value, value, vehicle_type_id, tariff_id) VALUES (1251, 1, 59, 0.60, 1052, 10);
INSERT INTO public.tariff_vehicle_type (id, min_value, max_value, value, vehicle_type_id, tariff_id) VALUES (1252, 1, 59, 0.75, 1053, 10);
INSERT INTO public.tariff_vehicle_type (id, min_value, max_value, value, vehicle_type_id, tariff_id) VALUES (4452, 1, 60, 0.50, 1052, 4402);
INSERT INTO public.tariff_vehicle_type (id, min_value, max_value, value, vehicle_type_id, tariff_id) VALUES (4851, 1, 59, 1.00, 1053, 4402);
INSERT INTO public.tariff_vehicle_type (id, min_value, max_value, value, vehicle_type_id, tariff_id) VALUES (6101, 1, 59, 0.75, 1052, 6051);


--
-- TOC entry 3332 (class 0 OID 61722)
-- Dependencies: 214
-- Data for Name: user_authority; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_authority (id, authority, active, user_id) VALUES (1151, 'ROLE_CASHIER', true, 1101);
INSERT INTO public.user_authority (id, authority, active, user_id) VALUES (4302, 'ROLE_CAJERO', true, 4252);
INSERT INTO public.user_authority (id, authority, active, user_id) VALUES (4660, 'ROLE_USER', true, 2);
INSERT INTO public.user_authority (id, authority, active, user_id) VALUES (5901, 'ROLE_CASHIER', true, 5851);


--
-- TOC entry 3333 (class 0 OID 61727)
-- Dependencies: 215
-- Data for Name: user_authority_institution; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_authority_institution (id, active, institution_id, user_authority_id) VALUES (1201, true, 2, 1151);
INSERT INTO public.user_authority_institution (id, active, institution_id, user_authority_id) VALUES (4352, true, 4205, 4302);
INSERT INTO public.user_authority_institution (id, active, institution_id, user_authority_id) VALUES (4701, true, 1, 4660);
INSERT INTO public.user_authority_institution (id, active, institution_id, user_authority_id) VALUES (5951, true, 5751, 5901);


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 223
-- Name: seq_demoparis; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_demoparis', 3, true);


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 221
-- Name: seq_parqueaderosancayetano; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_parqueaderosancayetano', 1, true);


--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 222
-- Name: seq_sancayetano; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.seq_sancayetano', 61, true);


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 198
-- Name: sequence_generator; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_generator', 6150, true);


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 220
-- Name: sequence_loja_bolivar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_loja_bolivar', 27, true);


--
-- TOC entry 3143 (class 2606 OID 61696)
-- Name: authority_functionality authority_functionality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authority_functionality
    ADD CONSTRAINT authority_functionality_pkey PRIMARY KEY (id);


--
-- TOC entry 3125 (class 2606 OID 61642)
-- Name: catalogue catalogue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.catalogue
    ADD CONSTRAINT catalogue_pkey PRIMARY KEY (id);


--
-- TOC entry 3145 (class 2606 OID 61701)
-- Name: contract contract_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contract
    ADD CONSTRAINT contract_pkey PRIMARY KEY (id);


--
-- TOC entry 3113 (class 2606 OID 61594)
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- TOC entry 3141 (class 2606 OID 61691)
-- Name: functionality functionality_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.functionality
    ADD CONSTRAINT functionality_pkey PRIMARY KEY (id);


--
-- TOC entry 3147 (class 2606 OID 61706)
-- Name: horary horary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horary
    ADD CONSTRAINT horary_pkey PRIMARY KEY (id);


--
-- TOC entry 3131 (class 2606 OID 61660)
-- Name: institution institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_pkey PRIMARY KEY (id);


--
-- TOC entry 3129 (class 2606 OID 61652)
-- Name: item_catalogue item_catalogue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_catalogue
    ADD CONSTRAINT item_catalogue_pkey PRIMARY KEY (id);


--
-- TOC entry 3121 (class 2606 OID 61619)
-- Name: jhi_authority jhi_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_authority
    ADD CONSTRAINT jhi_authority_pkey PRIMARY KEY (name);


--
-- TOC entry 3123 (class 2606 OID 61624)
-- Name: jhi_user_authority jhi_user_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT jhi_user_authority_pkey PRIMARY KEY (user_id, authority_name);


--
-- TOC entry 3115 (class 2606 OID 61610)
-- Name: jhi_user jhi_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT jhi_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3139 (class 2606 OID 61683)
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 3163 (class 2606 OID 61879)
-- Name: place place_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.place
    ADD CONSTRAINT place_pkey PRIMARY KEY (id);


--
-- TOC entry 3157 (class 2606 OID 61739)
-- Name: receipt receipt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_pkey PRIMARY KEY (id);


--
-- TOC entry 3165 (class 2606 OID 61904)
-- Name: record record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT record_pkey PRIMARY KEY (id);


--
-- TOC entry 3161 (class 2606 OID 61746)
-- Name: record_ticket record_ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record_ticket
    ADD CONSTRAINT record_ticket_pkey PRIMARY KEY (id);


--
-- TOC entry 3137 (class 2606 OID 61675)
-- Name: system_parameter_institution system_parameter_institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_parameter_institution
    ADD CONSTRAINT system_parameter_institution_pkey PRIMARY KEY (id);


--
-- TOC entry 3133 (class 2606 OID 61668)
-- Name: system_parameters system_parameters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_parameters
    ADD CONSTRAINT system_parameters_pkey PRIMARY KEY (id);


--
-- TOC entry 3149 (class 2606 OID 61716)
-- Name: tariff tariff_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tariff
    ADD CONSTRAINT tariff_pkey PRIMARY KEY (id);


--
-- TOC entry 3151 (class 2606 OID 61721)
-- Name: tariff_vehicle_type tariff_vehicle_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tariff_vehicle_type
    ADD CONSTRAINT tariff_vehicle_type_pkey PRIMARY KEY (id);


--
-- TOC entry 3155 (class 2606 OID 61731)
-- Name: user_authority_institution user_authority_institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_authority_institution
    ADD CONSTRAINT user_authority_institution_pkey PRIMARY KEY (id);


--
-- TOC entry 3153 (class 2606 OID 61726)
-- Name: user_authority user_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_authority
    ADD CONSTRAINT user_authority_pkey PRIMARY KEY (id);


--
-- TOC entry 3127 (class 2606 OID 61644)
-- Name: catalogue ux_catalogue__code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.catalogue
    ADD CONSTRAINT ux_catalogue__code UNIQUE (code);


--
-- TOC entry 3159 (class 2606 OID 61741)
-- Name: receipt ux_receipt__recordticketid_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT ux_receipt__recordticketid_id UNIQUE (recordticketid_id);


--
-- TOC entry 3135 (class 2606 OID 61670)
-- Name: system_parameters ux_system_parameters__code; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_parameters
    ADD CONSTRAINT ux_system_parameters__code UNIQUE (code);


--
-- TOC entry 3117 (class 2606 OID 61614)
-- Name: jhi_user ux_user_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_email UNIQUE (email);


--
-- TOC entry 3119 (class 2606 OID 61612)
-- Name: jhi_user ux_user_login; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user
    ADD CONSTRAINT ux_user_login UNIQUE (login);


--
-- TOC entry 3175 (class 2606 OID 61777)
-- Name: authority_functionality fk_authority_functionality__functionality_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authority_functionality
    ADD CONSTRAINT fk_authority_functionality__functionality_id FOREIGN KEY (functionality_id) REFERENCES public.functionality(id);


--
-- TOC entry 3166 (class 2606 OID 61625)
-- Name: jhi_user_authority fk_authority_name; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES public.jhi_authority(name);


--
-- TOC entry 3177 (class 2606 OID 61787)
-- Name: contract fk_contract__contractor_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contract
    ADD CONSTRAINT fk_contract__contractor_id FOREIGN KEY (contractor_id) REFERENCES public.person(id);


--
-- TOC entry 3176 (class 2606 OID 61782)
-- Name: contract fk_contract__status_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contract
    ADD CONSTRAINT fk_contract__status_id FOREIGN KEY (status_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3174 (class 2606 OID 61772)
-- Name: functionality fk_functionality__parent_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.functionality
    ADD CONSTRAINT fk_functionality__parent_id FOREIGN KEY (parent_id) REFERENCES public.functionality(id);


--
-- TOC entry 3179 (class 2606 OID 61797)
-- Name: horary fk_horary__contract_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horary
    ADD CONSTRAINT fk_horary__contract_id FOREIGN KEY (contract_id) REFERENCES public.contract(id);


--
-- TOC entry 3178 (class 2606 OID 61792)
-- Name: horary fk_horary__status_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.horary
    ADD CONSTRAINT fk_horary__status_id FOREIGN KEY (status_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3169 (class 2606 OID 61867)
-- Name: institution fk_institution__canton_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT fk_institution__canton_id FOREIGN KEY (canton_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3168 (class 2606 OID 61747)
-- Name: item_catalogue fk_item_catalogue__catalogue_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_catalogue
    ADD CONSTRAINT fk_item_catalogue__catalogue_id FOREIGN KEY (catalogue_id) REFERENCES public.catalogue(id);


--
-- TOC entry 3173 (class 2606 OID 61767)
-- Name: person fk_person__identification_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT fk_person__identification_type_id FOREIGN KEY (identification_type_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3172 (class 2606 OID 61762)
-- Name: person fk_person__user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT fk_person__user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


--
-- TOC entry 3192 (class 2606 OID 61885)
-- Name: place fk_place__institution_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.place
    ADD CONSTRAINT fk_place__institution_id FOREIGN KEY (institution_id) REFERENCES public.institution(id);


--
-- TOC entry 3191 (class 2606 OID 61880)
-- Name: place fk_place__status_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.place
    ADD CONSTRAINT fk_place__status_id FOREIGN KEY (status_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3186 (class 2606 OID 61847)
-- Name: receipt fk_receipt__recordticketid_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT fk_receipt__recordticketid_id FOREIGN KEY (recordticketid_id) REFERENCES public.record_ticket(id);


--
-- TOC entry 3189 (class 2606 OID 61862)
-- Name: record_ticket fk_record_ticket__institution_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record_ticket
    ADD CONSTRAINT fk_record_ticket__institution_id FOREIGN KEY (institution_id) REFERENCES public.institution(id);


--
-- TOC entry 3190 (class 2606 OID 61895)
-- Name: record_ticket fk_record_ticket__placeid_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record_ticket
    ADD CONSTRAINT fk_record_ticket__placeid_id FOREIGN KEY (placeid_id) REFERENCES public.place(id);


--
-- TOC entry 3187 (class 2606 OID 61852)
-- Name: record_ticket fk_record_ticket__status_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record_ticket
    ADD CONSTRAINT fk_record_ticket__status_id FOREIGN KEY (status_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3188 (class 2606 OID 61857)
-- Name: record_ticket fk_record_ticket__tariff_vehicle_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.record_ticket
    ADD CONSTRAINT fk_record_ticket__tariff_vehicle_id FOREIGN KEY (tariff_vehicle_id) REFERENCES public.tariff_vehicle_type(id);


--
-- TOC entry 3171 (class 2606 OID 61757)
-- Name: system_parameter_institution fk_system_parameter_institution__institution_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_parameter_institution
    ADD CONSTRAINT fk_system_parameter_institution__institution_id FOREIGN KEY (institution_id) REFERENCES public.institution(id);


--
-- TOC entry 3170 (class 2606 OID 61752)
-- Name: system_parameter_institution fk_system_parameter_institution__parameter_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.system_parameter_institution
    ADD CONSTRAINT fk_system_parameter_institution__parameter_id FOREIGN KEY (parameter_id) REFERENCES public.system_parameters(id);


--
-- TOC entry 3180 (class 2606 OID 61817)
-- Name: tariff fk_tariff__institution_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tariff
    ADD CONSTRAINT fk_tariff__institution_id FOREIGN KEY (institution_id) REFERENCES public.institution(id);


--
-- TOC entry 3182 (class 2606 OID 61827)
-- Name: tariff_vehicle_type fk_tariff_vehicle_type__tariff_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tariff_vehicle_type
    ADD CONSTRAINT fk_tariff_vehicle_type__tariff_id FOREIGN KEY (tariff_id) REFERENCES public.tariff(id);


--
-- TOC entry 3181 (class 2606 OID 61822)
-- Name: tariff_vehicle_type fk_tariff_vehicle_type__vehicle_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tariff_vehicle_type
    ADD CONSTRAINT fk_tariff_vehicle_type__vehicle_type_id FOREIGN KEY (vehicle_type_id) REFERENCES public.item_catalogue(id);


--
-- TOC entry 3183 (class 2606 OID 61832)
-- Name: user_authority fk_user_authority__user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_authority
    ADD CONSTRAINT fk_user_authority__user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


--
-- TOC entry 3184 (class 2606 OID 61837)
-- Name: user_authority_institution fk_user_authority_institution__institution_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_authority_institution
    ADD CONSTRAINT fk_user_authority_institution__institution_id FOREIGN KEY (institution_id) REFERENCES public.institution(id);


--
-- TOC entry 3185 (class 2606 OID 61842)
-- Name: user_authority_institution fk_user_authority_institution__user_authority_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_authority_institution
    ADD CONSTRAINT fk_user_authority_institution__user_authority_id FOREIGN KEY (user_authority_id) REFERENCES public.user_authority(id);


--
-- TOC entry 3167 (class 2606 OID 61630)
-- Name: jhi_user_authority fk_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jhi_user_authority
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.jhi_user(id);


-- Completed on 2022-01-10 11:59:49 -05

--
-- PostgreSQL database dump complete
--


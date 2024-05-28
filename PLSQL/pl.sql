--Funcio per a borrar
create or replace FUNCTION BorrarPilot(
    p_Nom IN VARCHAR2,
    p_Altura IN NUMBER,
    p_NumVictories IN NUMBER
    
) RETURN VARCHAR2 AUTHID CURRENT_USER IS 
BEGIN
    DELETE FROM ESCUDERIES WHERE Numero IN (SELECT Numero FROM PILOT WHERE NOM = p_Nom AND ALTURA = p_Altura AND NUMVICTORIES = p_NumVictories);
    DELETE FROM PILOT WHERE Nom = p_Nom AND ALTURA=p_Altura AND NUMVICTORIES=p_numvictories;

    IF SQL%ROWCOUNT > 0 THEN
        RETURN 'Pilot borrat amb èxit';
    ELSE
        RETURN 'Pilot no trobat';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 'S ha produït un error: ' || SQLERRM;
END BorrarPilot;

--Procediment per a crear les taules
create or replace PROCEDURE CreatePilotTaula AUTHID CURRENT_USER IS
BEGIN
    BEGIN
        EXECUTE IMMEDIATE 'SELECT * FROM PILOT';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            EXECUTE IMMEDIATE 'CREATE TABLE PILOT (
                Numero NUMBER(4),
                Nom VARCHAR2(50) PRIMARY KEY,
                altura NUMBER(4,2),
                campeoMundial NUMBER(1),
                numVictories NUMBER(3)
            )';
    WHEN OTHERS THEN
        IF SQLCODE = -942 THEN
            EXECUTE IMMEDIATE 'CREATE TABLE PILOT (
                Numero NUMBER(4),
                Nom VARCHAR2(50) PRIMARY KEY,
                altura NUMBER(4,2),
                campeoMundial NUMBER(1),
                numVictories NUMBER(3)


            )';
        ELSE
            -- Re-lanzar otras excepciones
            RAISE;
        END IF;
    END;
     BEGIN
        EXECUTE IMMEDIATE 'SELECT ESCUDERIA FROM ESCUDERIES';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            EXECUTE IMMEDIATE 'CREATE TABLE ESCUDERIES (
                Numero NUMBER(4) PRIMARY KEY,
                Escuderia VARCHAR2(50)
            )';
    WHEN OTHERS THEN
        IF SQLCODE = -942 THEN
            EXECUTE IMMEDIATE 'CREATE TABLE ESCUDERIES (
                Numero NUMBER(4) PRIMARY KEY,
                Escuderia VARCHAR2(50)
            )';
        ELSE
            -- Re-lanzar otras excepciones
            RAISE;
        END IF;
    END;
END CreatePilotTaula;

--Procediment per a insertar a la taula
create or replace PROCEDURE InsertarPilot(
    p_Nom IN VARCHAR2,
    p_Altura IN NUMBER,
    p_CampeoMundial IN NUMBER,
    p_NumVictories IN NUMBER,
    p_Escuderia IN VARCHAR2,
    p_Numero IN NUMBER
) AUTHID CURRENT_USER IS
BEGIN
    INSERT INTO PILOT (Numero, Nom, Altura, CampeoMundial, NumVictories )
    VALUES (p_Numero, p_Nom, p_Altura, p_CampeoMundial, p_NumVictories);

    INSERT INTO ESCUDERIES (Numero, Escuderia)
    VALUES ((SELECT max(NUMERO) FROM PILOT), p_Escuderia);

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END InsertarPilot;

--Trigger per a assignar el numero
create or replace TRIGGER assignar_numero
BEFORE INSERT ON PILOT
FOR EACH ROW
DECLARE max_numero INT DEFAULT 0;
BEGIN
    SELECT MAX(Numero) INTO max_numero FROM PILOT;
    IF max_numero IS NULL THEN
    :NEW.Numero := 1;
    ELSE
        :NEW.Numero := max_numero + 1;
        END IF;
END;

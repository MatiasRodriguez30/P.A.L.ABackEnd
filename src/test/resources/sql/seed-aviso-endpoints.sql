insert into reclutadores (
    nro_reclutador,
    cuil_reclutador,
    descripcion_reclutador,
    fecha_baja_reclutador,
    mail_reclutador,
    nombre_reclutador
) values (
    1,
    '20-30111222-3',
    'Reclutador de prueba',
    null,
    'reclu@test.com',
    'Juan Perez'
);

insert into empresas (
    nro_empresa,
    cuit_empresa,
    descripcion_empresa,
    direccion_empresa,
    fecha_alta_empresa,
    fecha_baja_empresa,
    mail_empresa,
    razon_social_empresa,
    telefono_empresa
) values (
    1,
    '30-71234567-8',
    'Empresa demo',
    'Calle 123',
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'empresa@test.com',
    'Tech SA',
    '1111-2222'
);

insert into reclutadores_empresas (
    contador_reclutador_empresa,
    fecha_fin_reclutador_empresa,
    fecha_inicio_reclutador_empresa,
    nro_empresa,
    nro_reclutador
) values (
    1,
    null,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    1,
    1
);

insert into carreras (
    cod_carrera,
    descripcion_carrera,
    fecha_actualizacion_carrera,
    fecha_alta_carrera,
    fecha_baja_carrera,
    nombre_carrera
) values (
    1,
    'Carrera sistemas',
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'Ingenieria en Sistemas'
);

insert into carreras (
    cod_carrera,
    descripcion_carrera,
    fecha_actualizacion_carrera,
    fecha_alta_carrera,
    fecha_baja_carrera,
    nombre_carrera
) values (
    2,
    'Carrera programacion',
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'Tecnicatura en Programacion'
);

insert into tipos_aviso (
    cod_tipo_aviso,
    descripcion_tipo_aviso,
    fecha_baja_tipo_aviso,
    fecha_hora_alta_tipo_aviso,
    nombre_tipo_aviso
) values (
    1,
    'Tipo de jornada',
    null,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    'Jornada'
);

insert into tipos_aviso (
    cod_tipo_aviso,
    descripcion_tipo_aviso,
    fecha_baja_tipo_aviso,
    fecha_hora_alta_tipo_aviso,
    nombre_tipo_aviso
) values (
    2,
    'Tipo de experiencia',
    null,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    'Experiencia'
);

insert into subtipos_aviso (
    nro_sub_tipo_aviso,
    cod_tipo_aviso,
    fecha_alta_sub_tipo_aviso,
    fecha_baja_sub_tipo_aviso,
    nombre_sub_tipo_aviso
) values (
    1,
    1,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'PartTime'
);

insert into subtipos_aviso (
    nro_sub_tipo_aviso,
    cod_tipo_aviso,
    fecha_alta_sub_tipo_aviso,
    fecha_baja_sub_tipo_aviso,
    nombre_sub_tipo_aviso
) values (
    2,
    1,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'FullTime'
);

insert into subtipos_aviso (
    nro_sub_tipo_aviso,
    cod_tipo_aviso,
    fecha_alta_sub_tipo_aviso,
    fecha_baja_sub_tipo_aviso,
    nombre_sub_tipo_aviso
) values (
    3,
    2,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'Junior'
);

insert into subtipos_aviso (
    nro_sub_tipo_aviso,
    cod_tipo_aviso,
    fecha_alta_sub_tipo_aviso,
    fecha_baja_sub_tipo_aviso,
    nombre_sub_tipo_aviso
) values (
    4,
    2,
    TIMESTAMP WITH TIME ZONE '2026-06-18 00:00:00+00',
    null,
    'Senior'
);

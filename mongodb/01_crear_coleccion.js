// ===================================================
// SCRIPTS DE MONGODB - ESTRUCTURA Y DATOS
// Proyecto: Sistema de Gestión de Pedidos
// Fecha: 30 de noviembre de 2025
// ===================================================

// Conectar a la base de datos
use sistema_pedidos;

// ===================================================
// CREAR COLECCIÓN CON VALIDACIÓN DE ESQUEMA
// ===================================================
db.createCollection("clientes_info", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["id_cliente", "comentarios", "preferencias"],
            properties: {
                id_cliente: {
                    bsonType: "int",
                    description: "ID del cliente (debe coincidir con PostgreSQL)"
                },
                comentarios: {
                    bsonType: "array",
                    description: "Array de comentarios del cliente",
                    items: {
                        bsonType: "object",
                        required: ["texto", "fecha"],
                        properties: {
                            texto: {
                                bsonType: "string",
                                description: "Texto del comentario"
                            },
                            fecha: {
                                bsonType: "date",
                                description: "Fecha del comentario"
                            },
                            producto_relacionado: {
                                bsonType: "int",
                                description: "ID del producto relacionado (opcional)"
                            },
                            calificacion: {
                                bsonType: "int",
                                minimum: 1,
                                maximum: 5,
                                description: "Calificación de 1 a 5 estrellas"
                            }
                        }
                    }
                },
                preferencias: {
                    bsonType: "object",
                    required: ["idioma", "metodo_pago", "notificaciones"],
                    properties: {
                        idioma: {
                            bsonType: "string",
                            enum: ["español", "ingles", "portugues", "frances"],
                            description: "Idioma preferido del cliente"
                        },
                        metodo_pago: {
                            bsonType: "string",
                            enum: ["tarjeta_credito", "tarjeta_debito", "paypal", "transferencia", "efectivo"],
                            description: "Método de pago habitual"
                        },
                        notificaciones: {
                            bsonType: "bool",
                            description: "Si desea recibir notificaciones"
                        },
                        categorias_favoritas: {
                            bsonType: "array",
                            description: "Categorías de productos favoritas",
                            items: {
                                bsonType: "string"
                            }
                        },
                        newsletter: {
                            bsonType: "bool",
                            description: "Si desea recibir newsletter"
                        }
                    }
                },
                historial_navegacion: {
                    bsonType: "array",
                    description: "Historial de productos vistos",
                    items: {
                        bsonType: "object",
                        properties: {
                            id_producto: {
                                bsonType: "int"
                            },
                            fecha_visita: {
                                bsonType: "date"
                            },
                            tiempo_visualizacion: {
                                bsonType: "int",
                                description: "Tiempo en segundos"
                            }
                        }
                    }
                }
            }
        }
    }
});

// Crear índices para mejorar el rendimiento
db.clientes_info.createIndex({ "id_cliente": 1 }, { unique: true });
db.clientes_info.createIndex({ "comentarios.fecha": -1 });
db.clientes_info.createIndex({ "preferencias.categorias_favoritas": 1 });

print("Colección 'clientes_info' creada exitosamente con validación de esquema");

// ===================================================
// INSERTAR DATOS DE PRUEBA
// ===================================================

// Cliente 1: Juan Pérez
db.clientes_info.insertOne({
    id_cliente: 1,
    comentarios: [
        {
            texto: "Excelente servicio, la laptop llegó en perfectas condiciones.",
            fecha: new Date("2024-11-02"),
            producto_relacionado: 1,
            calificacion: 5
        },
        {
            texto: "El mouse es muy cómodo, perfecto para trabajar todo el día.",
            fecha: new Date("2024-11-03"),
            producto_relacionado: 2,
            calificacion: 5
        },
        {
            texto: "El teclado mecánico tiene un sonido increíble.",
            fecha: new Date("2024-11-29"),
            producto_relacionado: 3,
            calificacion: 4
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "tarjeta_credito",
        notificaciones: true,
        categorias_favoritas: ["Electrónicos", "Accesorios"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 1,
            fecha_visita: new Date("2024-10-30"),
            tiempo_visualizacion: 180
        },
        {
            id_producto: 4,
            fecha_visita: new Date("2024-11-28"),
            tiempo_visualizacion: 120
        }
    ]
});

// Cliente 2: María García
db.clientes_info.insertOne({
    id_cliente: 2,
    comentarios: [
        {
            texto: "El monitor tiene excelente calidad de imagen.",
            fecha: new Date("2024-11-06"),
            producto_relacionado: 4,
            calificacion: 5
        },
        {
            texto: "Muy satisfecha con mi compra, todo perfecto.",
            fecha: new Date("2024-11-25"),
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "paypal",
        notificaciones: true,
        categorias_favoritas: ["Electrónicos", "Oficina"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 4,
            fecha_visita: new Date("2024-11-04"),
            tiempo_visualizacion: 240
        },
        {
            id_producto: 23,
            fecha_visita: new Date("2024-11-23"),
            tiempo_visualizacion: 90
        }
    ]
});

// Cliente 3: Carlos López
db.clientes_info.insertOne({
    id_cliente: 3,
    comentarios: [
        {
            texto: "La silla ergonómica es muy cómoda para trabajar desde casa.",
            fecha: new Date("2024-11-11"),
            producto_relacionado: 9,
            calificacion: 5
        },
        {
            texto: "El escritorio ajustable es justo lo que necesitaba.",
            fecha: new Date("2024-11-12"),
            producto_relacionado: 10,
            calificacion: 4
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "tarjeta_debito",
        notificaciones: false,
        categorias_favoritas: ["Oficina", "Ergonomía"],
        newsletter: false
    },
    historial_navegacion: [
        {
            id_producto: 9,
            fecha_visita: new Date("2024-11-08"),
            tiempo_visualizacion: 300
        },
        {
            id_producto: 10,
            fecha_visita: new Date("2024-11-09"),
            tiempo_visualizacion: 200
        }
    ]
});

// Cliente 4: Ana Martínez
db.clientes_info.insertOne({
    id_cliente: 4,
    comentarios: [
        {
            texto: "Los auriculares tienen cancelación de ruido increíble.",
            fecha: new Date("2024-11-13"),
            producto_relacionado: 6,
            calificacion: 5
        },
        {
            texto: "La mochila es perfecta para llevar mi laptop al trabajo.",
            fecha: new Date("2024-11-14"),
            producto_relacionado: 13,
            calificacion: 4
        }
    ],
    preferencias: {
        idioma: "ingles",
        metodo_pago: "tarjeta_credito",
        notificaciones: true,
        categorias_favoritas: ["Electrónicos", "Accesorios"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 6,
            fecha_visita: new Date("2024-11-10"),
            tiempo_visualizacion: 150
        }
    ]
});

// Cliente 5: Pedro Rodríguez
db.clientes_info.insertOne({
    id_cliente: 5,
    comentarios: [
        {
            texto: "La licencia de Office funciona perfectamente.",
            fecha: new Date("2024-11-16"),
            producto_relacionado: 17,
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "transferencia",
        notificaciones: true,
        categorias_favoritas: ["Software"],
        newsletter: false
    },
    historial_navegacion: []
});

// Cliente 6: Laura Sánchez
db.clientes_info.insertOne({
    id_cliente: 6,
    comentarios: [
        {
            texto: "Los libros son muy educativos y bien escritos.",
            fecha: new Date("2024-11-19"),
            producto_relacionado: 19,
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "efectivo",
        notificaciones: false,
        categorias_favoritas: ["Libros", "Educación"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 19,
            fecha_visita: new Date("2024-11-17"),
            tiempo_visualizacion: 100
        },
        {
            id_producto: 20,
            fecha_visita: new Date("2024-11-17"),
            tiempo_visualizacion: 80
        }
    ]
});

// Cliente 7: Diego Torres
db.clientes_info.insertOne({
    id_cliente: 7,
    comentarios: [
        {
            texto: "La cámara web 4K tiene excelente calidad de video.",
            fecha: new Date("2024-11-21"),
            producto_relacionado: 21,
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "paypal",
        notificaciones: true,
        categorias_favoritas: ["Electrónicos", "Video"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 21,
            fecha_visita: new Date("2024-11-19"),
            tiempo_visualizacion: 220
        },
        {
            id_producto: 22,
            fecha_visita: new Date("2024-11-19"),
            tiempo_visualizacion: 180
        }
    ]
});

// Cliente 8: Sofia Ramírez
db.clientes_info.insertOne({
    id_cliente: 8,
    comentarios: [
        {
            texto: "Muy contenta con mi nueva laptop, funciona perfectamente.",
            fecha: new Date("2024-11-23"),
            producto_relacionado: 1,
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "tarjeta_credito",
        notificaciones: true,
        categorias_favoritas: ["Electrónicos", "Tecnología"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 1,
            fecha_visita: new Date("2024-11-20"),
            tiempo_visualizacion: 350
        }
    ]
});

// Cliente 10: Carmen Díaz
db.clientes_info.insertOne({
    id_cliente: 10,
    comentarios: [
        {
            texto: "El router WiFi 6 mejoró mi conexión significativamente.",
            fecha: new Date("2024-11-27"),
            producto_relacionado: 24,
            calificacion: 5
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "tarjeta_debito",
        notificaciones: false,
        categorias_favoritas: ["Redes", "Tecnología"],
        newsletter: false
    },
    historial_navegacion: []
});

// Cliente 11: Roberto Morales
db.clientes_info.insertOne({
    id_cliente: 11,
    comentarios: [
        {
            texto: "Los mouses son de excelente calidad.",
            fecha: new Date("2024-11-28"),
            producto_relacionado: 2,
            calificacion: 4
        }
    ],
    preferencias: {
        idioma: "español",
        metodo_pago: "efectivo",
        notificaciones: true,
        categorias_favoritas: ["Accesorios"],
        newsletter: false
    },
    historial_navegacion: []
});

// Cliente 12: Patricia Cruz
db.clientes_info.insertOne({
    id_cliente: 12,
    comentarios: [],
    preferencias: {
        idioma: "español",
        metodo_pago: "tarjeta_credito",
        notificaciones: true,
        categorias_favoritas: ["Oficina", "Iluminación"],
        newsletter: true
    },
    historial_navegacion: [
        {
            id_producto: 11,
            fecha_visita: new Date("2024-11-27"),
            tiempo_visualizacion: 60
        }
    ]
});

print("\n===================================");
print("DATOS DE PRUEBA INSERTADOS");
print("===================================");
print("Total de documentos: " + db.clientes_info.countDocuments());
print("===================================\n");

// ===================================================
// CONSULTAS DE EJEMPLO
// ===================================================

print("=== EJEMPLO 1: Buscar cliente por ID ===");
printjson(db.clientes_info.findOne({ id_cliente: 1 }));

print("\n=== EJEMPLO 2: Clientes que desean notificaciones ===");
db.clientes_info.find(
    { "preferencias.notificaciones": true },
    { id_cliente: 1, "preferencias.idioma": 1, "preferencias.metodo_pago": 1 }
).forEach(printjson);

print("\n=== EJEMPLO 3: Comentarios con calificación 5 estrellas ===");
db.clientes_info.find(
    { "comentarios.calificacion": 5 },
    { id_cliente: 1, "comentarios.$": 1 }
).forEach(printjson);

print("\n=== EJEMPLO 4: Clientes con preferencia de pago PayPal ===");
db.clientes_info.find(
    { "preferencias.metodo_pago": "paypal" },
    { id_cliente: 1, "preferencias.metodo_pago": 1 }
).forEach(printjson);

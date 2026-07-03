// ===================================================
// CONSULTAS AVANZADAS DE MONGODB
// ===================================================

use sistema_pedidos;

print("\n╔════════════════════════════════════════════════╗");
print("║   CONSULTAS AVANZADAS - MONGODB                ║");
print("╚════════════════════════════════════════════════╝\n");

// ===================================================
// 1. AGREGACIÓN: Estadísticas de comentarios por cliente
// ===================================================
print("=== 1. Estadísticas de comentarios por cliente ===");
db.clientes_info.aggregate([
    {
        $project: {
            id_cliente: 1,
            total_comentarios: { $size: "$comentarios" },
            promedio_calificacion: { $avg: "$comentarios.calificacion" }
        }
    },
    { $sort: { total_comentarios: -1 } }
]).forEach(printjson);

// ===================================================
// 2. Clientes con más de 2 comentarios
// ===================================================
print("\n=== 2. Clientes con más de 2 comentarios ===");
db.clientes_info.find(
    { $expr: { $gt: [{ $size: "$comentarios" }, 2] } },
    { id_cliente: 1, comentarios: 1 }
).forEach(printjson);

// ===================================================
// 3. Buscar comentarios por texto (búsqueda de texto)
// ===================================================
print("\n=== 3. Comentarios que contienen 'excelente' ===");
db.clientes_info.find(
    { "comentarios.texto": { $regex: /excelente/i } },
    { id_cliente: 1, "comentarios.texto": 1 }
).forEach(printjson);

// ===================================================
// 4. Clientes por categorías favoritas
// ===================================================
print("\n=== 4. Clientes interesados en Electrónicos ===");
db.clientes_info.find(
    { "preferencias.categorias_favoritas": "Electrónicos" },
    { id_cliente: 1, "preferencias.categorias_favoritas": 1 }
).forEach(printjson);

// ===================================================
// 5. Agregar nuevo comentario a un cliente
// ===================================================
print("\n=== 5. Agregar comentario al cliente 1 ===");
db.clientes_info.updateOne(
    { id_cliente: 1 },
    {
        $push: {
            comentarios: {
                texto: "Segundo pedido realizado, todo perfecto nuevamente.",
                fecha: new Date(),
                calificacion: 5
            }
        }
    }
);
print("Comentario agregado exitosamente");

// ===================================================
// 6. Actualizar preferencias de un cliente
// ===================================================
print("\n=== 6. Actualizar preferencias del cliente 3 ===");
db.clientes_info.updateOne(
    { id_cliente: 3 },
    {
        $set: {
            "preferencias.notificaciones": true,
            "preferencias.newsletter": true
        }
    }
);
print("Preferencias actualizadas");

// ===================================================
// 7. Agregar producto al historial de navegación
// ===================================================
print("\n=== 7. Agregar producto visto al historial del cliente 5 ===");
db.clientes_info.updateOne(
    { id_cliente: 5 },
    {
        $push: {
            historial_navegacion: {
                id_producto: 1,
                fecha_visita: new Date(),
                tiempo_visualizacion: 240
            }
        }
    }
);
print("Historial actualizado");

// ===================================================
// 8. Estadísticas generales de la colección
// ===================================================
print("\n=== 8. Estadísticas generales ===");
db.clientes_info.aggregate([
    {
        $group: {
            _id: null,
            total_clientes: { $sum: 1 },
            total_comentarios: { $sum: { $size: "$comentarios" } },
            clientes_con_notificaciones: {
                $sum: { $cond: ["$preferencias.notificaciones", 1, 0] }
            },
            clientes_con_newsletter: {
                $sum: { $cond: ["$preferencias.newsletter", 1, 0] }
            }
        }
    }
]).forEach(printjson);

// ===================================================
// 9. Comentarios recientes (últimos 7 días)
// ===================================================
print("\n=== 9. Comentarios de los últimos 7 días ===");
var fechaLimite = new Date();
fechaLimite.setDate(fechaLimite.getDate() - 7);

db.clientes_info.aggregate([
    { $unwind: "$comentarios" },
    { $match: { "comentarios.fecha": { $gte: fechaLimite } } },
    {
        $project: {
            id_cliente: 1,
            "comentarios.texto": 1,
            "comentarios.fecha": 1,
            "comentarios.calificacion": 1
        }
    },
    { $sort: { "comentarios.fecha": -1 } }
]).forEach(printjson);

// ===================================================
// 10. Distribución de métodos de pago
// ===================================================
print("\n=== 10. Distribución de métodos de pago ===");
db.clientes_info.aggregate([
    {
        $group: {
            _id: "$preferencias.metodo_pago",
            cantidad: { $sum: 1 }
        }
    },
    { $sort: { cantidad: -1 } }
]).forEach(printjson);

// ===================================================
// 11. Clientes por idioma
// ===================================================
print("\n=== 11. Distribución de idiomas ===");
db.clientes_info.aggregate([
    {
        $group: {
            _id: "$preferencias.idioma",
            cantidad: { $sum: 1 }
        }
    },
    { $sort: { cantidad: -1 } }
]).forEach(printjson);

// ===================================================
// 12. Productos más comentados
// ===================================================
print("\n=== 12. Productos más comentados ===");
db.clientes_info.aggregate([
    { $unwind: "$comentarios" },
    { $match: { "comentarios.producto_relacionado": { $exists: true } } },
    {
        $group: {
            _id: "$comentarios.producto_relacionado",
            cantidad_comentarios: { $sum: 1 },
            calificacion_promedio: { $avg: "$comentarios.calificacion" }
        }
    },
    { $sort: { cantidad_comentarios: -1 } },
    { $limit: 10 }
]).forEach(printjson);

// ===================================================
// 13. Buscar cliente específico con toda su información
// ===================================================
print("\n=== 13. Información completa del cliente 1 ===");
printjson(db.clientes_info.findOne({ id_cliente: 1 }));

// ===================================================
// 14. Actualizar múltiples documentos
// ===================================================
print("\n=== 14. Agregar categoría favorita a clientes con español ===");
var resultado = db.clientes_info.updateMany(
    { "preferencias.idioma": "español" },
    { $addToSet: { "preferencias.categorias_favoritas": "Ofertas" } }
);
print("Documentos modificados: " + resultado.modifiedCount);

// ===================================================
// 15. Eliminar comentarios antiguos (ejemplo - no ejecutar)
// ===================================================
print("\n=== 15. Ejemplo de eliminación de comentarios antiguos ===");
print("// db.clientes_info.updateMany(");
print("//     {},");
print("//     { $pull: { comentarios: { fecha: { $lt: new Date('2024-01-01') } } } }");
print("// );");

print("\n╔════════════════════════════════════════════════╗");
print("║   CONSULTAS COMPLETADAS EXITOSAMENTE          ║");
print("╚════════════════════════════════════════════════╝\n");

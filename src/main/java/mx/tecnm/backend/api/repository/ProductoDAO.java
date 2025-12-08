package mx.tecnm.backend.api.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import mx.tecnm.backend.api.models.Producto;

@Repository
public class ProductoDAO {

    @Autowired
    private JdbcClient conexion;

    public List<Producto> consultarProductos() {
        String sql = "SELECT id, nombre, precio, sku, color, marca, descripcion, categorias_id FROM productos";
        return conexion.sql(sql)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .list();
    }

    public Producto crearProducto(String nombre, double precio, String sku, String color, String marca, String descripcion, int categorias_id) {
        String sql = "INSERT INTO productos (nombre, precio, sku, color, marca, descripcion, categorias_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, nombre, precio, sku, color, marca, descripcion, categorias_id";
        return conexion.sql(sql)
            .param(nombre)
            .param(precio)
            .param(sku)
            .param(color)
            .param(marca)
            .param(descripcion)
            .param(categorias_id)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .single();
    }

    public List<Producto> busquedaID(int id) {
        String sql = "SELECT id, nombre, precio, sku, color, marca, descripcion, categorias_id FROM productos WHERE id = ?";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .list();
    }

    public Producto actualizarProducto(int id, String nombre, double precio, String sku, String color, String marca, String descripcion, int categorias_id) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, sku = ?, color = ?, marca = ?, descripcion = ?, categorias_id = ? WHERE id = ? RETURNING id, nombre, precio, sku, color, marca, descripcion, categorias_id";
        return conexion.sql(sql)
            .param(nombre)
            .param(precio)
            .param(sku)
            .param(color)
            .param(marca)
            .param(descripcion)
            .param(categorias_id)
            .param(id)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .single();
    }

    public List<Producto> desactivar(int id) {
        String sql = "DELETE FROM productos WHERE id = ? RETURNING id, nombre, precio, sku, color, marca, descripcion, categorias_id";
        return conexion.sql(sql)
            .param(id)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .list();
    }
    
    public double obtenerPrecioProducto(int producto_id) {
        String sql = "SELECT precio FROM productos WHERE id = ?";
        return conexion.sql(sql)
            .param(producto_id)
            .query(Double.class)
            .single();
    }
    
    public List<Producto> consultarProductosPorCategoria(int categoria_id) {
        String sql = "SELECT id, nombre, precio, sku, color, marca, descripcion, categorias_id FROM productos WHERE categorias_id = ?";
        return conexion.sql(sql)
            .param(categoria_id)
            .query((rs, rowNum) -> new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getString("sku"),
                rs.getString("color"),
                rs.getString("marca"),
                rs.getString("descripcion"),
                rs.getInt("categorias_id")))
            .list();
        }
}
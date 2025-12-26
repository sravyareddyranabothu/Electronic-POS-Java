package electronicspos.dao;

import electronicspos.db.DBConnection;
import electronicspos.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ================= ADD PRODUCT =================
    public boolean addProduct(Product p) {

        String sql = "INSERT INTO product " +
                "(product_id, product_name, category, price, stock, image_path) " +
                "VALUES (product_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagePath()); // ⭐ IMAGE PATH

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= UPDATE PRODUCT =================
    public boolean updateProduct(Product p) {

        String sql = "UPDATE product SET product_name=?, category=?, price=?, stock=?, image_path=? " +
                     "WHERE product_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getImagePath());
            ps.setInt(6, p.getProductId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= DELETE PRODUCT =================
    public boolean deleteProduct(int productId) {

        String sql = "DELETE FROM product WHERE product_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 // ================= GET PRODUCT BY NAME =================
    public Product getProductByName(String name) {

        String sql = "SELECT * FROM product WHERE product_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("image_path")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // ================= GET ALL PRODUCTS =================
    public List<Product> getAllProducts() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT * FROM product ORDER BY product_id";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("image_path") // ⭐ IMAGE
                );
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

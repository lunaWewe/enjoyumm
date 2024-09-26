package tw.luna.FinalTest.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="products")
@EntityListeners(AuditingEntityListener.class)  // 啟用實體的審計功能，以自動賦值或更新時間
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productId")
	private Integer productId;
	
	@Column(name = "type", columnDefinition = "enum('mealkit','preparedFood')")
	private String type;
	
	@Column(name = "sku", nullable = false, length = 50)
	private String sku;
	
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "price", nullable = false)
	private Integer price;
	
	@ManyToOne
	@JoinColumn(name = "categoryId", referencedColumnName = "categoryId" ,nullable = false)
    @JsonBackReference
	private Category category;
	
	@Column(name = "stockQuantity", nullable = false)
	private Integer stockQuantity;
	
	@Column(name = "isDel", columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isDel;

	// 新增一對多關係，商品可以有多張圖片
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> images;

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public Integer getStockQuantity() {
		return stockQuantity;
	}


	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Boolean getIsDel() {
		return isDel;
	}


	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}
	
	
}

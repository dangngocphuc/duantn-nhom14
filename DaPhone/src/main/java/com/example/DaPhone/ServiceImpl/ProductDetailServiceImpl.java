package com.example.DaPhone.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Imei;
import com.example.DaPhone.Entity.ProductDetail;
import com.example.DaPhone.Entity.ProductDetailValue;
import com.example.DaPhone.Repository.ProductDetailRepo;
import com.example.DaPhone.Request.ProductDetailRequest;
import com.example.DaPhone.Service.ProductDetailService;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

	@Autowired
	private ProductDetailRepo productRepo;
	
	@Autowired
	private CommonUtils commonUtils;

	@Override
	public Page<ProductDetail> findProduct(ProductDetailRequest productParam, Pageable pageable) {
		Page<ProductDetail> listPage = productRepo.findAll(new Specification<ProductDetail>() {
			@Override
			public Predicate toPredicate(Root<ProductDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (ProductDetail pro : listPage) {
			if(!pro.getListImei().isEmpty()) {
				pro.setQuantity(pro.getListImei().stream().filter(e -> e.getStatus()==1).count());
			}
			pro.setListProductDetailValue(null);
			pro.getProduct().setListProductDetail(null);
			pro.getProduct().setListProductOption(null);
		}
		return listPage;
	}

	@Override
	public ProductDetail findById(Long id) {
		ProductDetail product = productRepo.getById(id);
//		return toResponse(product);
		return product;
	}

	public ProductDetail toResponse(ProductDetail entity) {
		ProductDetail item = new ProductDetail();
		item.setId(entity.getId());
		item.setProductName(entity.getProductName());
		if (entity.getProduct() != null) {
			entity.getProduct().setListProductDetail(null);
		}
		item.setProduct(entity.getProduct());
		if (entity.getListProductDetailValue() != null) {
			for (ProductDetailValue e : entity.getListProductDetailValue()) {
				e.setProductDetail(null);
				if (e.getOptionValue() != null) {
//					e.getOptionValue().setListProductDetailValue(null);
				}
				if (e.getOption() != null) {
//					e.getOption().setListProductDetailValue(null);
				}
			}
			item.setListProductDetailValue(entity.getListProductDetailValue());
		}
		return item;
	}

	@Override
	public boolean saveOrUpdate(ProductDetail productDetail) {
		for (ProductDetailValue productDetailValue : productDetail.getListProductDetailValue()) {
			productDetailValue.setProductDetail(productDetail);
		}
		ProductDetail pro = productRepo.save(productDetail);
		return true;
	}

	@Override
	public List<ProductDetail> getListProduct() {
		List<ProductDetail> listPage = productRepo.findAll(new Specification<ProductDetail>() {
			@Override
			public Predicate toPredicate(Root<ProductDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		for (ProductDetail pro : listPage) {
			pro.setListProductDetailValue(null);
			pro.getProduct().setListProductDetail(null);
			pro.getProduct().setListProductOption(null);
		}
		return listPage;
	}
}

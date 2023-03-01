package com.fpoly.datn.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Brand;
import com.fpoly.datn.entity.Cpu;
import com.fpoly.datn.entity.Gpu;
import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.entity.Product;
import com.fpoly.datn.entity.ProductDetail;
import com.fpoly.datn.entity.Ram;
import com.fpoly.datn.entity.Rom;
import com.fpoly.datn.repository.ProductDetailRepo;
import com.fpoly.datn.request.ProductDetailRequest;
import com.fpoly.datn.service.ProductDetailService;

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
				Join<ProductDetail, Product> joinProduct = root.join("product", JoinType.LEFT);
				Join<ProductDetail, Imei> joinImei = root.join("listImei", JoinType.LEFT);
				Join<Product, Brand> joinBrand = joinProduct.join("brand", JoinType.LEFT);
				
				Join<ProductDetail, Cpu> joinCpu = root.join("cpu", JoinType.LEFT);
				Join<ProductDetail, Gpu> joinGpu = root.join("gpu", JoinType.LEFT);
				Join<ProductDetail, Ram> joinRam = root.join("ram", JoinType.LEFT);
				Join<ProductDetail, Rom> joinRom = root.join("rom", JoinType.LEFT);
				
//				Join<ProductDetail, ProductDetailValue> joinProductDetailValue = root.join("listProductDetailValue",
//						JoinType.LEFT);
//				Join<ProductDetailValue, OptionValue> joinOptionValue = joinProductDetailValue.join("optionValue",
//						JoinType.LEFT);
				List<Predicate> predicates = new ArrayList<>();
				if (productParam.getProductId() != null) {
					String productId = productParam.getProductId();
					if (productId.contains(",")) {
						String[] productIds = productId.split(",");
						Expression<String> parentExpressionTT = joinProduct.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(productIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinProduct.get("id"), productId)));
					}
				}
				if (productParam.getBrandId() != null) {
					String brandId = productParam.getBrandId();
					if (brandId.contains(",")) {
						String[] brandIds = brandId.split(",");
						Expression<String> parentExpressionTT = joinBrand.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(brandIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinBrand.get("id"), brandId)));
					}
				}
				
				if (productParam.getLstCpu() != null) {
					String cpuId = productParam.getLstCpu();
					if (cpuId.contains(",")) {
						String[] cpuIds = cpuId.split(",");
						Expression<String> parentExpressionTT = joinCpu.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(cpuIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinCpu.get("id"), cpuId)));
					}
				}
				
				if (productParam.getLstGpu() != null) {
					String gpuId = productParam.getLstGpu();
					if (gpuId.contains(",")) {
						String[] gpuIds = gpuId.split(",");
						Expression<String> parentExpressionTT = joinGpu.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(gpuIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinGpu.get("id"), gpuId)));
					}
				}
				
				if (productParam.getLstRam() != null) {
					String ramId = productParam.getLstRam();
					if (ramId.contains(",")) {
						String[] ramIds = ramId.split(",");
						Expression<String> parentExpressionTT = joinRam.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(ramIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinRam.get("id"), ramId)));
					}
				}
				if (productParam.getLstRom() != null) {
					String romId = productParam.getLstRom();
					if (romId.contains(",")) {
						String[] romIdIds = romId.split(",");
						Expression<String> parentExpressionTT = joinRom.get("id");
						Predicate parentPredicateTT = parentExpressionTT.in(romIdIds);
						predicates.add(cb.and(parentPredicateTT));
					} else {
						predicates.add(cb.and(cb.equal(joinRom.get("id"), romId)));
					}
				}
//				if (productParam.getOptionValueID() != null) {
//					String optonValueId = productParam.getOptionValueID();
//					if (optonValueId.contains(",")) {
//						String[] optonValueIds = optonValueId.split(",");
//						Expression<String> parentExpressionTT = joinOptionValue.get("id");
//						Predicate parentPredicateTT = parentExpressionTT.in(optonValueIds);
//						predicates.add(cb.and(parentPredicateTT));
//					} else {
//						predicates.add(cb.and(cb.equal(joinOptionValue.get("id"), optonValueId)));
//					}
//				}
				if (productParam.getProductCode() != null && !productParam.getProductCode().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("productCode")),
							"%" + productParam.getProductCode().trim().toUpperCase() + "%")));
				}

				if (productParam.getProductName() != null && !productParam.getProductName().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("productName")),
							"%" + productParam.getProductName().trim().toUpperCase() + "%")));
				}

				if (productParam.getPriceFrom() > 0) {
					predicates.add(
							cb.and(cb.greaterThanOrEqualTo(root.get("productPrice"), productParam.getPriceFrom())));
				}
				if (productParam.getPriceTo() > 0) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("productPrice"), productParam.getPriceTo())));
				}
				
				if (productParam.getToDate() != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("createDate"),
							productParam.getToDate()));
				}
				if (productParam.getFromDate() != null) {
//					Calendar calendar = commonUtils.dateToCalendar(hoSoUyQuyen.getDenNgay());
//					calendar.add(Calendar.DAY_OF_MONTH, 1);
					predicates.add(cb.lessThan(root.<Date>get("createDate"),
							new Date(productParam.getFromDate().getTime() + (1000 * 60 * 60 * 24))));
				}
				
				
				if (productParam.isInventory()) {
					predicates.add(cb.and(cb.greaterThan(root.get("productQuantily"), 0)));
				}
				predicates.add(cb.and(cb.equal(joinImei.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (ProductDetail pro : listPage) {
			if (!pro.getListImei().isEmpty()) {
				pro.setQuantity(pro.getListImei().stream().filter(e -> e.getStatus() == 1).count());
				for (Imei i : pro.getListImei()) {
					i.setBillDetail(null);
				}
			}
//			pro.setListProductDetailValue(null);
			pro.getProduct().setListProductDetail(null);
//			pro.getProduct().setListProductOption(null);
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
//		if (entity.getListProductDetailValue() != null) {
//			for (ProductDetailValue e : entity.getListProductDetailValue()) {
//				e.setProductDetail(null);
//				if (e.getOptionValue() != null) {
////					e.getOptionValue().setListProductDetailValue(null);
//				}
//				if (e.getOption() != null) {
////					e.getOption().setListProductDetailValue(null);
//				}
//			}
//			item.setListProductDetailValue(entity.getListProductDetailValue());
//		}
		return item;
	}

	@Override
	public boolean saveOrUpdate(ProductDetail productDetail) {
		if (productDetail.getId() != null) {
			ProductDetail pro = productRepo.findById(productDetail.getId()).get();
//			pro.setProductCode(CommonUtils.generateProductCode());
			pro.setUpdateDate(new Date());
			pro.getListImei().clear();
			pro.getListImei().addAll(productDetail.getListImei());
			pro = productRepo.save(pro);
		} else {
			productDetail.setCreateDate(new Date());
			productDetail.setProductCode(CommonUtils.generateProductCode());
			ProductDetail pro = productRepo.save(productDetail);
		}
		return true;
	}

	@Override
	public List<ProductDetail> getListProduct() {
		List<ProductDetail> listPage = productRepo.findAll(new Specification<ProductDetail>() {
			@Override
			public Predicate toPredicate(Root<ProductDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				Join<ProductDetail, Imei> joinImei = root.join("listImei", JoinType.LEFT);
				predicates.add(cb.and(cb.equal(joinImei.get("status"), 1)));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		for (ProductDetail pro : listPage) {
			if (!pro.getListImei().isEmpty()) {
				pro.setQuantity(pro.getListImei().stream().filter(e -> e.getStatus() == 1).count());
			}
//			pro.setListProductDetailValue(null);
			pro.getProduct().setListProductDetail(null);
//			pro.getProduct().setListProductOption(null);
		}
		return listPage;
	}

	@Override
	public String compareLaptops(List<ProductDetail> listProductDetail) {
		ProductDetail ProductDetail1 = productRepo.getById(listProductDetail.get(0).getId());
		ProductDetail ProductDetail2 = productRepo.getById(listProductDetail.get(1).getId());
		double avgRating1 = ProductDetail1.calculateAverageRating();
		double avgRating2 = ProductDetail2.calculateAverageRating();
		String result = null;
		if (avgRating1 > avgRating2) {
			result = ProductDetail1.getProductName() + " có đánh giá người dùng tốt hơn " + " "
					+ ProductDetail2.getProductName();
		} else if (avgRating1 < avgRating2) {
			result = ProductDetail2.getProductName() + " có đánh giá người dùng tốt hơn "
					+ ProductDetail1.getProductName();
		} else {
			result = ProductDetail1.getProductName() + " and " + ProductDetail2.getProductName() + " "
					+ " có đánh giá bằng nhau";
		}
		return result;
	}

}

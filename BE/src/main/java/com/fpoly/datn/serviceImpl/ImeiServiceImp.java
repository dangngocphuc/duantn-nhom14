package com.fpoly.datn.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.entity.ProductDetail;
import com.fpoly.datn.repository.ImeiRepository;
import com.fpoly.datn.repository.ProductDetailRepo;
import com.fpoly.datn.request.ImeiRequest;
import com.fpoly.datn.service.ImeiService;

@Service
public class ImeiServiceImp implements ImeiService {

	@Autowired
	private ImeiRepository imeiRepo;

	@Autowired
	private ProductDetailRepo productDetailRepo;

	@Override
	public Page<Imei> getPageImei(ImeiRequest request, Pageable pageable) {
		Page<Imei> page = imeiRepo.findAll(new Specification<Imei>() {
			@Override
			public Predicate toPredicate(Root<Imei> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Imei, ProductDetail> join = root.join("productDetail", JoinType.LEFT);
				List<Predicate> predicates = new ArrayList<>();
				if (request.getImei() != null && !request.getImei().equals("")) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("imei")),
							"%" + request.getImei().trim().toUpperCase() + "%")));
				}
				if (request.getProductId() != null) {
					predicates.add(cb.and(cb.equal(join.get("id"), request.getProductId())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);

		for (Imei imei : page) {
			if (imei.getProductDetail() != null) {
				imei.getProductDetail().setListImei(null);
				imei.getProductDetail().setProduct(null);
//				imei.getProductDetail().setListProductDetailValue(null);
				imei.setProductName(imei.getProductDetail().getProductName());
			}
			imei.setBillDetail(null);
		}

		return page;
	}

	@Override
	public Imei findById(Long id) {
		Imei imei = imeiRepo.findById(id).get();
		return imei;
	}

	@Override
	public boolean saveImei(Imei imei) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteImei(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Imei> getListImeiByProductDetail(ImeiRequest req, Pageable pageable) {
		Page<Imei> page = imeiRepo.findAll(new Specification<Imei>() {
			@Override
			public Predicate toPredicate(Root<Imei> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (req != null) {
					if (req.getImei() != null && !(req.getImei().equals(""))) {
						predicates.add(
								criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("imei")),
										"%" + req.getImei().trim().toUpperCase() + "%")));
					}
					if (req.getProductId() != null && !(req.getProductId().equals(""))) {
						predicates.add(criteriaBuilder
								.and(criteriaBuilder.equal(root.get("productDetail").get("id"), req.getProductId())));
					}
				}

				predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), 1)));
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return page.getContent();
	}

	@SuppressWarnings({ "resource", "deprecation" })
	@Override
	public Map<String, List> readFileExcel(MultipartFile[] files) throws Exception {
		// TODO Auto-generated method stub
		Map<String, List> mResult = new HashMap<String, List>();

		try {
			for (MultipartFile inFile : files) {
				InputStream inFileStream = inFile.getInputStream();
//				InputStream inputStream = new FileInputStream(inFile);
				Workbook workbook = null;
				try {
					workbook = new XSSFWorkbook(inFileStream);
				} catch (Exception e) {
					throw new IllegalArgumentException("The specified file is not Excel file");
				}
				Sheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();

				List<Imei> lstSuccess = new ArrayList<Imei>();
				List<String> lstError = new ArrayList<String>();

				while (rowIterator.hasNext()) {

					Row row = rowIterator.next();
					// bỏ header
					if (row.getRowNum() < 1)
						continue;
					if (CommonUtils.checkIfRowIsEmpty(row)) {
						lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + ". Không được để trống.");
						lstSuccess.removeAll(lstSuccess);
						break;
						
					}
					Imei imei = new Imei();
					imei.setStatus(CommonUtils.TrangThai.CON.getValue());
					Iterator<Cell> cellIterator = row.cellIterator();
					try {
						Boolean bolHasError = false;
						int iColumns = 0;
//						String strCtmt = StringUtils.EMPTY;
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
//							String sValue = "";
							switch (cell.getColumnIndex()) {
							case 0: // mã sản phẩm
								try {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String maSanPham = (String) CommonUtils.returnCellValue(cell);
									if (StringUtils.isEmpty(maSanPham.trim())) {
										bolHasError = true;
										lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
												+ (cell.getColumnIndex() + 1) + ". Không được để trống.");
										lstSuccess.removeAll(lstSuccess);
										continue;
									}
									boolean bolExist = false;
									if (maSanPham != null) {
										ProductDetail productDetail = productDetailRepo.getByMa(maSanPham);
										if (productDetail != null) {
											imei.setProductDetail(productDetail);
											iColumns++;
											bolExist = true;
											break;
										} else {
											bolHasError = true;
											lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
													+ (cell.getColumnIndex() + 1) + ". Mã sản phẩm không tồn tại.");
											lstSuccess.removeAll(lstSuccess);
											continue;
										}
									}
								} catch (Exception e) {
									bolHasError = true;
									lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
											+ (cell.getColumnIndex() + 1));
									lstSuccess.removeAll(lstSuccess);
									continue;
								}

								break;
							case 1: // tên sản phẩm
								try {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String tenSanPham = (String) CommonUtils.returnCellValue(cell);
									if (StringUtils.isEmpty(tenSanPham.trim())) {
										bolHasError = true;
										lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
												+ (cell.getColumnIndex() + 1) + ". Không được để trống.");
										lstSuccess.removeAll(lstSuccess);
										continue;
									}
									boolean bolExist = false;
									if (tenSanPham != null) {
										iColumns++;
										bolExist = true;
										break;
									}
								} catch (Exception e) {
									bolHasError = true;
									lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
											+ (cell.getColumnIndex() + 1));
									lstSuccess.removeAll(lstSuccess);
									continue;
								}
								break;
							case 2: // Imei
								try {
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String imeis = (String) CommonUtils.returnCellValue(cell);
									if (StringUtils.isEmpty(imeis.trim())) {
										bolHasError = true;
										lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
												+ (cell.getColumnIndex() + 1) + ". Không được để trống.");
										lstSuccess.removeAll(lstSuccess);
										continue;
									}
									boolean bolExist = false;
									if (imeis != null) {

										Imei imeiExist = imeiRepo.getByImei(imeis);
										if (imeiExist != null) {
											bolHasError = true;
											lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
													+ (cell.getColumnIndex() + 1) + ". Imei đã tồn tại.");
											lstSuccess.removeAll(lstSuccess);
											continue;
										} else {
											imei.setImei(imeis);
											iColumns++;
											bolExist = true;
											break;
										}
									}
								} catch (Exception e) {
									bolHasError = true;
									lstError.add("Lỗi hàng thứ: " + (row.getRowNum() + 1) + " cột thứ: "
											+ (cell.getColumnIndex() + 1));
									lstSuccess.removeAll(lstSuccess);
									continue;
								}
								break;
							}

						}
						if (bolHasError == false && iColumns == 3) {
							lstSuccess.add(imei);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				mResult.put("SUCCESS", lstSuccess);
				mResult.put("ERROR", lstError);
			}
		} catch (Exception e) {
			throw e;
		}
		return mResult;
	}

	@Override
	public boolean saveListImei(List<Imei> listImei) {
		imeiRepo.saveAll(listImei);
		return true;
	}

}

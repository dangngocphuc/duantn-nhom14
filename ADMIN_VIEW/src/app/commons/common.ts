export enum ROLE {
  ADMIN = 'ADMIN',
  USER = 'USER'
}

export enum ERROR {
  SUCCESS = '00',
  FAILURE = '01'
}

export enum Action {
  THEMMOI = 0,
  CAPNHAT = 1,
  XEM = 2,
  XOA = 3,
  COPY = 4,
  LICHSUCHINHSUA = 5,
}

export class Common {

  lstTrangThai = [
    { value: 0, name: 'Hết' },
    { value: 1, name: 'Còn hàng' },
  ]

  lstDemand = [
    { value: '1', name: 'Văn phòng, học tập ' },
    { value: '2', name: '2D Design' },
    { value: '3', name: 'Quay dựng video' },
    { value: '4', name: '3D Design' },
    { value: '5', name: 'Gaming' },
    { value: '6', name: 'Lập trình' },
  ]

  lstTrangThaiImei = [
    { value: 0, name: 'Mới'},
    { value: 1, name: 'Đã bán' },
    { value: 2, name: 'Đang vận chuyển:'},
    { value: 3, name: 'Lỗi' },
    { value: 4, name: 'Hàng Demo' },
    { value: 5, name: 'Đã trả nhà cung cấp' },
    { value: 6, name: 'Đang bảo hành' },
    { value: 7, name: 'Đã bảo hành' },
  ]

  lstSupplier = [
    { value: 1, name: 'HANA'},
    { value: 2, name: 'Qualcomm' },
    { value: 3, name: 'TSMC' },
  ]
}

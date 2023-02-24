export enum ROLE{
  ADMIN = 'ADMIN',
  USER = 'USER'
}

export enum ERROR{
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
    {value : 0 ,name :'hết '},
    {value : 1 ,name :'Còn hàng'},
  ]
}

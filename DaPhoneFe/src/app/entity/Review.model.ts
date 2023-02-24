import { ProductDetail } from "../models/type";
import { Product } from "./Product";

export class Review {
  reviewID: number;
  productDetail: ProductDetail;
  reviewName: string;
  userId: Number;
  reviewStar: number;
  reviewMessage: string;
  date : Date;
  }
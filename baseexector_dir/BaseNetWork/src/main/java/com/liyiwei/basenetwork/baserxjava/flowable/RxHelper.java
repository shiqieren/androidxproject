package com.liyiwei.basenetwork.baserxjava.flowable;


public class RxHelper {
//    public static <T> FlowableTransformer<Response<T>, T> VChatFlowableTransformer() {
//        return new FlowableTransformer<Response<T>, T>() {
//            @Override
//            public Publisher<T> apply(Flowable<Response<T>> upstream) {
//                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                        .flatMap(new Function<Response<T>, Publisher<T>>() {
//                            @Override
//                            public Publisher<T> apply(Response<T> tResponse) throws Exception {
//                                if (tResponse.getMsgCode() == 200) {
//                                    if (tResponse.isPage() && tResponse.getTotal() == 0) {
//                                        return Flowable.error(new EmptyException());
//                                    }
//                                    if (tResponse.isPage() && tResponse.getIndex() != 1 && (tResponse.getData() == null || ((List) tResponse.getData()).isEmpty())) {
//                                        return Flowable.error(new NoMoreDataException());
//                                    }
//                                    if (tResponse.getData() == null) {
//                                        return Flowable.error(new NoDataException());
//                                    }
//                                    return Flowable.just(tResponse.getData());
//                                } else {
//                                    return Flowable.error(new VChatException(tResponse.getMsgCode(), tResponse.getMsg()));
//                                }
//                            }
//                        });
//            }
//        };
//    }
//
//    public static <T> FlowableTransformer<Response<T>, Response<T>> VChatTransformer() {
//        return new FlowableTransformer<Response<T>, Response<T>>() {
//            @Override
//            public Publisher<Response<T>> apply(Flowable<Response<T>> upstream) {
//                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                        .flatMap(new Function<Response<T>, Publisher<Response<T>>>() {
//                            @Override
//                            public Publisher<Response<T>> apply(Response<T> tResponse) throws Exception {
//                                if (tResponse.getMsgCode() == 200) {
//                                    if (tResponse.isPage() && (tResponse.getData() == null || ((List) tResponse.getData()).isEmpty())) {
//                                        return Flowable.error(new NoMoreDataException());
//                                    }
//                                    return Flowable.just(tResponse);
//                                } else {
//                                    return Flowable.error(new VChatException(tResponse.getMsgCode(), tResponse.getMsg()));
//                                }
//                            }
//                        });
//            }
//        };
//    }
//
//    public static <T> FlowableTransformer<ScanResponse<T>, ScanResponse<T>> ScanTransformer() {
//        return new FlowableTransformer<ScanResponse<T>, ScanResponse<T>>() {
//            @Override
//            public Publisher<ScanResponse<T>> apply(Flowable<ScanResponse<T>> upstream) {
//                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                        .flatMap(new Function<ScanResponse<T>, Publisher<ScanResponse<T>>>() {
//                            @Override
//                            public Publisher<ScanResponse<T>> apply(ScanResponse<T> tResponse) throws Exception {
//                                if (tResponse.getMsgCode() == 200) {
//                                    return Flowable.just(tResponse);
//                                } else {
//                                    return Flowable.error(new VChatException(tResponse.getMsgCode(), tResponse.getMsg()));
//                                }
//                            }
//                        });
//            }
//        };
//    }
//
//public static <T, K> FlowableTransformer<ExtraResponse<T, K>, ExtraResponse<T, K>> VChatExtraTransformer() {
//    return new FlowableTransformer<ExtraResponse<T, K>, ExtraResponse<T, K>>() {
//        @Override
//        public Publisher<ExtraResponse<T, K>> apply(Flowable<ExtraResponse<T, K>> upstream) {
//            return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                    .flatMap(new Function<ExtraResponse<T, K>, Publisher<ExtraResponse<T, K>>>() {
//                        @Override
//                        public Publisher<ExtraResponse<T, K>> apply(ExtraResponse<T, K> tResponse) throws Exception {
//                            if (tResponse.getMsgCode() == 200) {
//                                if (tResponse.isPage() && tResponse.getIndex() == 0 && tResponse.getTotal() == 0) {
//                                    return Flowable.error(new EmptyException());
//                                }
//                                if (tResponse.isPage() && tResponse.getIndex() != 1 && (tResponse.getData() == null || ((List) tResponse.getData()).isEmpty())) {
//                                    return Flowable.error(new NoMoreDataException());
//                                }
//                                return Flowable.just(tResponse);
//                            } else {
//                                return Flowable.error(new VChatException(tResponse.getMsgCode(), tResponse.getMsg()));
//                            }
//                        }
//                    });
//        }
//    };
//}
}
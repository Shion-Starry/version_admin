package app.desty.chat_admin.common.aop.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import app.desty.chat_admin.common.aop.annotation.UseMainThread;
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.core.ObservableEmitter;
//import io.reactivex.rxjava3.core.ObservableOnSubscribe;
//import io.reactivex.rxjava3.core.Scheduler;
//import io.reactivex.rxjava3.schedulers.Schedulers;

//@Aspect
public class ThreadAspect {
//    @Around("execution(@app.desty.common.aop.annotation.UseMainThread * *(..)) && @annotation(mainThread)")
//    public void pointMethod(ProceedingJoinPoint joinPoint, UseMainThread mainThread) {
//        boolean isMainThread = mainThread.value();
//        Scheduler scheduler = AndroidSchedulers.mainThread();
//        if(!isMainThread){
//            scheduler = Schedulers.io();
//        }
//        Observable.create(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
//                try {
//                    joinPoint.proceed();
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
//        }).subscribeOn(scheduler).subscribe();
//    }
}

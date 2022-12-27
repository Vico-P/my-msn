import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { SharedModule } from './modules/shared.module';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './component/login/login.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { MyMsnInterceptor } from './interceptor/mymsn.interceptor';
import { TokenGuard } from './guard/token.guard';

@NgModule({
  declarations: [AppComponent, LoginComponent],
  imports: [BrowserModule, HttpClientModule, SharedModule, AppRoutingModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: MyMsnInterceptor, multi: true },
    TokenGuard,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

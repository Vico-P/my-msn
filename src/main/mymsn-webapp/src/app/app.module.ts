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
import { NotFoundComponent } from './component/not-found/not-found.component';
import { RegisterComponent } from './component/register/register.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './component/home/home.component';
import { VerifyEmailComponent } from './component/verify-email/verify-email.component';
import { ResetPasswordFinishComponent } from './component/reset-password/finish/reset-password-finish.component';
import { ResetPasswordInitComponent } from './component/reset-password/init/reset-password-init.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NotFoundComponent,
    RegisterComponent,
    HomeComponent,
    VerifyEmailComponent,
    ResetPasswordFinishComponent,
    ResetPasswordInitComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule,
    BrowserAnimationsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: MyMsnInterceptor, multi: true },
    TokenGuard,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

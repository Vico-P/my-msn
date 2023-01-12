import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { AppRoutingModule } from './app-routing.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { MyMsnInterceptor } from './interceptor/mymsn.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './component/home/home.component';
import { AppLayoutModule } from './layout/app.layout.module';
import { NotFoundComponent } from './component/not-found/not-found.component';

@NgModule({
  declarations: [AppComponent, NotFoundComponent, HomeComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    AppLayoutModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: MyMsnInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

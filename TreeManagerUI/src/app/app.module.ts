import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TreeComponent } from './tree/tree.component';
import { HttpClientModule } from "@angular/common/http";
import {OrganizationChartModule} from "primeng/organizationchart";
import {ButtonModule} from "primeng/button";
import {PanelModule} from "primeng/panel";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SidebarModule} from "primeng/sidebar";
import { NodeComponent } from './node/node.component';

@NgModule({
  declarations: [
    AppComponent,
    TreeComponent,
    NodeComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    OrganizationChartModule,
    PanelModule,
    ButtonModule,
    FormsModule,
    SidebarModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

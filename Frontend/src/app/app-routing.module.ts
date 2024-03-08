import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { RegisterComponent } from './register/register.component';
import { UpdateComponent } from './update/update.component';

const routes: Routes = [
  { path: 'list', component: LayoutComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  { path: 'update', component: UpdateComponent },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

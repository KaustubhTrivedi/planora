import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { UsersModule } from './users/users.module';
import { TripsModule } from './trips/trips.module';
import { AiModule } from './ai/ai.module';
import { LocationModule } from './location/location.module';
import { CollaborationModule } from './collaboration/collaboration.module';
import { FilesModule } from './files/files.module';
import { NotificationsModule } from './notifications/notifications.module';
import { ChecklistsModule } from './checklists/checklists.module';
import { CacheModule } from './cache/cache.module';
import { DatabaseModule } from './database/database.module';
import { StorageModule } from './storage/storage.module';
import { IntegrationsModule } from './integrations/integrations.module';

@Module({
  controllers: [AppController],
  providers: [AppService],
  imports: [AuthModule, UsersModule, TripsModule, AiModule, LocationModule, CollaborationModule, FilesModule, NotificationsModule, ChecklistsModule, CacheModule, DatabaseModule, StorageModule, IntegrationsModule],
})
export class AppModule {}
